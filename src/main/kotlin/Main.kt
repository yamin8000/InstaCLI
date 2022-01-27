package yamin

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import com.github.instagram4j.instagram4j.models.media.timeline.ImageCarouselItem
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineCarouselMedia
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineImageMedia
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.models.user.User
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import kotlinx.coroutines.*
import utils.printlnC
import yamin.helpers.*
import yamin.helpers.LoggerHelper.loading
import yamin.helpers.LoggerHelper.loggerD
import yamin.helpers.LoggerHelper.loggerE
import yamin.helpers.LoggerHelper.progress
import yamin.utils.ConsoleHelper.getBooleanInput
import yamin.utils.ConsoleHelper.getIntegerInput
import yamin.utils.ConsoleHelper.getMultipleStrings
import yamin.utils.ConsoleHelper.pressEnterToContinue
import yamin.utils.Constants.YES
import yamin.utils.Constants.loginMenu
import yamin.utils.Constants.mainMenu
import yamin.utils.Constants.sleepDelay
import yamin.utils.JsonUtils.pretty
import yamin.utils.Settings
import yamin.utils.Utility.now
import java.io.File
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

typealias Dyad<T> = Pair<T, Throwable?>

private lateinit var requestHelper: RequestHelper

private lateinit var igClient: IGClient

private val scanner = Scanner(System.`in`)

fun main() {
    try {
        initLogin()
    } catch (logInException: IGLoginException) {
        printlnC { "Login failed: ${logInException.message}".red.bold }
        initLogin()
    } catch (exception: Exception) {
        loggerD("General Error!")
        loggerE(exception.stackTraceToString())
        return
    }
}

private fun initLogin() {
    igClient = loginHandler() ?: exitProcess(0)
    requestHelper = RequestHelper(igClient)
    previewCurrentUserInfo()
    mainMenuHandler()
}

fun loginHandler(): IGClient? {
    printlnC { "Welcome to ".green.bold + "InstaKiller".green.bright.bold }
    printlnC { loginMenu.cyan.bold }
    printlnC { "Please login first:".blue.bold }
    return when (scanner.getIntegerInput()) {
        0 -> loginHandler()
        1 -> getClientByUsernamePassword()
        2 -> getClientBySession() ?: loginHandler()
        3 -> {
            printlnC { "Bye!".red.bold }
            null
        }
        else -> {
            printlnC { "Invalid input! Please try again.".red.bold }
            loginHandler()
        }
    }
}

private fun mainMenuHandler() {
    scanner.pressEnterToContinue()
    showMainMenu()
    if (::igClient.isInitialized) {
        when (scanner.getIntegerInput()) {
            0 -> showMainMenu()
            1 -> handleShowingUserInfo()
            2 -> handleSearchingUser()
            3 -> handleUserPostsFetcher()
            4 -> handleSendingDirectMessage()
            5 -> handleGetFriends()
            6 -> handlePostsImagesDownloader()
            7 -> downloadUsersProfilePictures(scanner.getMultipleStrings("username"))
            9 -> Settings(scanner)
            10 -> {
                printlnC { "Bye!".bold.red }
                exitProcess(0)
            }
            else -> printlnC { "Invalid menu input!".red.bold }
        }
        mainMenuHandler()
    } else loginHandler()
}

private fun downloadUsersProfilePictures(usernames: List<String>) {
    val downloader = Downloader(igClient.httpClient)
    usernames.forEach { username ->
        File("images/$username").mkdirs()
        val (user, error) = UserHelper(igClient).getUserInfoByUsername(username)
        if (user != null && error == null) {
            val imageUrl = user.hd_profile_pic_url_info.url
            val imageName = imageUrl.substringAfterLast("/").substringBefore("?")
            val (imageFile, downloadError) = downloader.download(imageUrl, "images/$username/$imageName")
            if (imageFile != null && downloadError == null) {
                printlnC { "Image saved successfully to images/$username/$imageName".green.bright }
            } else printlnC { "Failed to download image for $username => ${downloadError?.message}".red.bold }
        } else printlnC { "Skipping, User with username $username not found! => ${error?.message}".red.bold }
    }
}

fun handleSearchingUser() {
    TODO("Not yet implemented")
}

fun handleShowingUserInfo() {
    printlnC { "Please enter the username: ".blue.bold }
    val username = scanner.nextLine()
    val (userInfo, error) = UserHelper(igClient).getUserInfoByUsername(username)
    if (userInfo != null && error == null) {
        showShortUserInfo(userInfo)
        checkIfMoreUserInfoNeeded(userInfo)
    } else printlnC { "Failed to get user info! Error: ${error?.message}".red.bold }
}

private fun checkIfMoreUserInfoNeeded(userInfo: User) {
    printlnC { "Do you want to see full user info? (y/n)".blue }
    if (scanner.getBooleanInput()) showFullUserInfo(userInfo)
}

private fun handlePostsImagesDownloader() {
    val postsHelper = PostsHelper(igClient)
    printlnC { "Please enter the username or usernames of the user/users you want to download posts from:".green }
    printlnC { "Separate usernames with a comma (,)".red.bright }
    val usernames = scanner.nextLine().split(",")
    val total = usernames.size
    usernames.forEachIndexed { index, username ->
        val (userInfo, userInfoError) = UserHelper(igClient).getUserInfoByUsername(username)
        if (userInfo != null && userInfoError == null) {
            showShortUserInfo(userInfo)
            printlnC { "${now()} ===> Downloading posts from $username <==> ${index + 1}/$total".yellow }
            val posts = mutableListOf<TimelineMedia>()
            val (userPosts, userPostsError) = postsHelper.getUserFeed(username)
            if (userPosts != null && userPostsError == null) {
                posts.addAll(userPosts)
            } else printlnC { "Failed to get user posts! Error: ${userPostsError?.message}".red.bold }
            saveImages(posts, username)
        } else {
            printlnC { "Failed to get user info! Error: ${userInfoError?.message}".red.bold }
            return
        }
    }
    printlnC { "${now()} ===> Done!".bold.green }
}

private fun handleGetFriends() {
    printlnC { "Enter instagram username to see friends".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "Choose friends' type, Followers = 1, Followings = 2 (1/2)?".blue.bright }
    val typeInput = scanner.getIntegerInput()
    printlnC { "Enter the number of friends you want to see (default is all available)?".blue.bright }
    val limit = scanner.getIntegerInput()
    val friendsHelper = FriendsHelper(igClient)
    val friendType = when (typeInput) {
        1 -> FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
        2 -> FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING
        else -> FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
    }
    progress {
        val (followers, error) = friendsHelper.getFriends(username, friendType, limit)
        it()
        if (followers != null && error == null) {
            if (followers.isNotEmpty()) {
                printlnC { "Friends:".blue.bright }
                followers.forEachIndexed { index, profile -> printlnC { "${index + 1}. ${profile.username} => ${profile.full_name}" } }
            } else printlnC { "No friends found!".red.bright }
        } else printlnC { "Failed to get friends! Error: ${error?.message}".red.bold }
    }
}

private fun handleSendingDirectMessage() {
    printlnC { "Enter usernames you want to send message. (multiple usernames are seperated by comma (,)): ".blue.bright }
    val usernames = scanner.nextLine().trim().split(",")
    printlnC { "Enter message you want to send:".blue.bright }
    val message = scanner.nextLine().trim()
    if (message.isNotBlank()) {
        val userHelper = UserHelper(igClient)
        usernames.forEach { username ->
            val (pk, error) = userHelper.getPk(username)
            if (pk != null && error == null) sendSingleDirectMessage(message, pk, username)
            else printlnC { "Skipping, Failed to get pk of $username! Error: ${error?.message}".red.bold }
        }

    } else {
        printlnC { "Message is empty, try again!".red.bold }
        //todo checking if this `return` is necessary
        return
    }
}

private fun sendSingleDirectMessage(message: String, pk: Long, username: String) {
    //val messageLoading = loadingAsync()
    val isDataSent = requestHelper.sendDirectMessageByPks(message, pk)
    if (isDataSent) printlnC { "${now()} ===> Message successfully sent to ".green.bright + username.blue.bright.bold }
    //messageLoading.cancel()
}

private fun showMainMenu() = printlnC { mainMenu.cyan.bold }

private fun getClientBySession(): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    printlnC { "Choose your account: ".blue.bright }
    sessions.forEachIndexed { index, name -> println("$index. $name") }
    val userInput = scanner.getIntegerInput()
    if (userInput in sessions.indices) {
        val clientFile = File("sessions/${sessions[userInput]}/client.ser")
        val cookieFile = File("sessions/${sessions[userInput]}/cookie.ser")
        printlnC { "${now()} ===> Login success!".green.bright }
        return IGClient.deserialize(clientFile, cookieFile)
    }
    return null
}

private fun handleUserPostsFetcher() {
    printlnC { "Enter instagram username to see posts: ".blue.bright }
    val targetUsername = scanner.nextLine().trim()

    loading {
        val (posts, error) = PostsHelper(igClient).getUserFeed(targetUsername)
        it()

        if (posts != null && error == null) {
            if (posts.isNotEmpty()) {
                printlnC { "${now()} ===> \n${posts.size}".green.bright + " posts have been fetched, enter number of posts you want to see: ".green }
                val count = scanner.getIntegerInput()
                if (count != -1) printlnC { posts.take(count).pretty().green.bright }

                printlnC { "Do you want to save posts' images as files? (y/n)".blue.bright }
                val isSavingImages = scanner.nextLine().trim().lowercase(Locale.getDefault()) == YES
                if (isSavingImages) saveImages(posts, targetUsername)
            } else printlnC { "($targetUsername) has no posts!".bold.red }
        } else printlnC { "Failed to get posts! Error: ${error?.message}".red.bold }
    }
}

private fun saveImages(posts: List<TimelineMedia>, targetUsername: String, indicator: Pair<Int, Int>? = null) {
    val jobs = mutableListOf<Job>()
    posts.forEach { timelineMedia ->
        when (timelineMedia) {
            is TimelineCarouselMedia -> {
                timelineMedia.carousel_media.forEach { item ->
                    if (item is ImageCarouselItem) {
                        jobs.add(
                            CoroutineScope(Dispatchers.IO).launch {
                                saveSingleImage(targetUsername, item)
                            }
                        )
                    }
                }
            }
            is TimelineImageMedia -> {
                jobs.add(
                    CoroutineScope(Dispatchers.IO).launch {
                        saveSingleImage(targetUsername, timelineMedia)
                    }
                )
            }
            else -> printlnC { "${now()} ===> Unsupported media type".red.bold }
        }
    }
    CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            if (jobs.all { it.isCompleted }) {
                val text = if (indicator == null) {
                    "${now()} ===> All images of ($targetUsername) have been saved!"
                } else {
                    "${now()} ===> part ${indicator.first} of ${indicator.second} ($targetUsername) images have been saved!"
                }
                printlnC { text.green }
                cancel()
                break
            }
            delay(sleepDelay)
        }
    }
}

private fun saveSingleImage(targetUsername: String, media: Any) {
    File("images/$targetUsername").mkdirs()
    val imageUrl = getImageUrl(media)
    if (imageUrl != null) {
        val imageName = imageUrl.substringAfterLast("/").substringBefore("?")
        val imageFile = File("images/$targetUsername/$imageName")
        if (!imageFile.exists()) {
            val image = URL(imageUrl).readBytes()
            imageFile.writeBytes(image)
            printlnC { "${now()} ===> ($targetUsername) -> Image saved successfully to images/$targetUsername/$imageName".green.bright }
        } else printlnC { "${now()} ===> ($targetUsername) -> Image (${imageName}) already exists, skipping...".yellow.bright }
    } else printlnC { "${now()} ===> ($targetUsername) -> Image url is null, skipping...".yellow.bright }
}

private fun getImageUrl(media: Any): String? {
    return when (media) {
        is ImageCarouselItem -> media.image_versions2.candidates.first().url
        is TimelineImageMedia -> media.image_versions2.candidates.first().url
        else -> null
    }
}

private fun getClientByUsernamePassword(): IGClient {
    val enterField = "Enter instagram "
    printlnC { "$enterField username: ".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "$enterField password: ".blue.bright }
    val password = scanner.nextLine().trim()

    val client = LoginHelper.logInWithChallenge(username, password)

    if (client.isLoggedIn) createSessionFiles(client, username)
    else printlnC { "${now()} ===> Login failed!".red.bold }

    return client
}

private fun previewCurrentUserInfo() {
    loading {
        val (userInfo, error) = UserHelper(igClient).getCurrentUserInfo()
        it()
        if (userInfo != null && error == null) showShortUserInfo(userInfo) else printlnC { "User info is null".red.bold }
    }
}

private fun showShortUserInfo(user: User) {
    printlnC { "${now()} ===> User info:".green.bright }

    printlnC { "Full name: ".green + user.full_name.green.bright.bold }
    printlnC { "Username: ".green + user.username.green.bright.bold }
    printlnC { "Bio: ".green + user.biography.green.bright.bold }
    printlnC { "Media Count: ".green + user.media_count.green.bright.bold }
    printlnC { "Follower Count: ".green + user.follower_count.green.bright.bold }
    printlnC { "Following Count: ".green + user.following_count.green.bright.bold }
}

private fun showFullUserInfo(user: User) {
    showShortUserInfo(user)
    printlnC { "Profile Url: ".green + "https://instagram.com/${user.username}".green.bright }
    printlnC { "Profile Pic Url: ".green + user.profile_pic_url.green.bright }
    printlnC { "Private or Public: ".green + (if (user.is_private) "Private" else "Public").green.bright }
    printlnC { "Verified or not (Blue tick): ".green + (if (user.is_verified) "Verified" else "Not Verified").green.bright }
    printlnC { "Business or Regular: ".green + (if (user.is_business) "Business" else "Regular").green.bright }
    printlnC { "External Url: ".green + user.external_url.green.bright }
}

private fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}