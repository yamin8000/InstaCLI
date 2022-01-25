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
import yamin.utils.CONSTANTS.POST_LIMIT_COUNT
import yamin.utils.CONSTANTS.YES
import yamin.utils.CONSTANTS.menuText
import yamin.utils.CONSTANTS.sleepDelay
import yamin.utils.JsonUtils.pretty
import yamin.utils.LoggerHelper.loadingAsync
import yamin.utils.LoggerHelper.loggerD
import yamin.utils.LoggerHelper.loggerE
import yamin.utils.LoginHelper
import yamin.utils.RequestHelper
import yamin.utils.Utility.now
import java.io.File
import java.net.URL
import java.util.*

private lateinit var requestHelper: RequestHelper

private val scanner = Scanner(System.`in`)

fun main() {
    loggerD("Start!")
    try {
        menuHandler()
    } catch (logInException: IGLoginException) {
        println("Login failed!")
        loggerE(logInException.stackTraceToString())
    } catch (exception: Exception) {
        loggerD("General Error!")
        loggerE(exception.stackTraceToString())
    }
}

private fun menuHandler() {
    var client: IGClient? = null

    showMenuText()
    do {
        when (getIntegerInput()) {
            0 -> showMenuText()
            1 -> {
                client = getClientByUsernamePassword()
                requestHelper = RequestHelper(client)
                previewCurrentUserInfo()
            }
            2 -> {
                client = getClientBySession()
                if (client != null) {
                    requestHelper = RequestHelper(client)
                    previewCurrentUserInfo()
                } else printlnC { "There's no saved sessions".bold.red }
            }
            3 -> {
                if (client != null) handleUserPostsFetcher()
                else printlnC { "Please login first!".bold.red }
            }
            4 -> {
                if (client != null) handleSendingDirectMessage()
                else printlnC { "Please login first!".bold.red }
            }
            5 -> {
                if (client != null) handleGetFriends()
                else printlnC { "Please login first!".bold.red }
            }
            6 -> {
                if (client != null) handlePostsImagesDownloader()
                else printlnC { "Please login first!".bold.red }
            }
            else -> printlnC { "Invalid menu input!".bold.red }
        }
        continue
    } while (true)
}

private fun handlePostsImagesDownloader() {
    printlnC { "Please enter the username or usernames of the user/users you want to download posts from:".green }
    printlnC { "Separate usernames with a comma (,)".red.bright }
    val usernames = scanner.nextLine().split(",")
    val total = usernames.size
    usernames.forEachIndexed { index, username ->
        val userInfoLoading = loadingAsync()
        val userInfo = requestHelper.getPk(username)?.let { requestHelper.getUserInfo(it) }
        userInfoLoading.cancel()
        val getPostsLoading = loadingAsync()
        printlnC { "${now()} ===> Downloading posts from $username <==> ${index + 1}/$total".yellow }
        if (userInfo != null) printUserInfo(userInfo)
        val mediaCount = userInfo?.media_count ?: 0
        val posts = mutableListOf<TimelineMedia>()
        if (mediaCount <= POST_LIMIT_COUNT) {
            posts.addAll(requestHelper.getUserFeed(username))
            getPostsLoading.cancel()
            saveImages(posts, username)
        } else {
            val parts = (mediaCount / POST_LIMIT_COUNT) + 1
            var nextMaxId: String? = null
            var i = 0
            while (i < mediaCount) {
                val userFeed = requestHelper.getRawUserFeed(username, nextMaxId = nextMaxId, limit = POST_LIMIT_COUNT)
                i += userFeed.first.size
                posts.addAll(userFeed.first)
                nextMaxId = userFeed.second
                saveImages(posts, username, i / POST_LIMIT_COUNT to parts)
            }
            getPostsLoading.cancel()
        }
    }
    printlnC { "${now()} ===> Done!".bold.green }
}

private fun handleGetFriends() {
    printlnC { "Enter instagram username to see friends".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "Choose friends' type, Followers = 1, Followings = 2 (1/2)?".blue.bright }
    val typeInput = getIntegerInput()
    val friendshipType =
        if (typeInput == 1) FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
        else FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
    printlnC {
        requestHelper.getUserFriends(
            username,
            friendshipsType = friendshipType
        ).pretty().green.bright
    }
}

private fun handleSendingDirectMessage() {
    printlnC { "Enter usernames you want to send message. (multiple usernames are seperated by comma (,)): ".blue.bright }
    val usernames = scanner.nextLine().trim().split(",")
    printlnC { "Enter message you want to send:".blue.bright }
    val message = scanner.nextLine().trim()
    if (message.isNotBlank()) {
        val pkLoading = loadingAsync()
        val pks = usernames.map { requestHelper.getPk(it) }
        pkLoading.cancel()
        if (pks.isNotEmpty()) {
            pks.forEachIndexed { index, pk ->
                if (pk != null)
                    sendSingleDirectMessage(message, pk, usernames[index])
            }
        }
    } else {
        printlnC { "Username or message is empty, try again!".bold.red }
        //todo checking if this `return` is necessary
        return
    }
}

private fun sendSingleDirectMessage(message: String, pk: Long, username: String) {
    val messageLoading = loadingAsync()
    val isDataSent = requestHelper.sendDirectMessageByPks(message, pk)
    if (isDataSent) printlnC { "${now()} ===> Message successfully sent to ".green.bright + username.blue.bright.bold }
    messageLoading.cancel()
}

private fun showMenuText() = printlnC { menuText.green.bold }

private fun getIntegerInput(): Int {
    return try {
        scanner.nextLine().trim().toInt()
    } catch (exception: NumberFormatException) {
        -1
    }
}

private fun getClientBySession(): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    printlnC { "Choose your account: ".blue.bright }
    sessions.forEachIndexed { index, name -> println("$index. $name") }
    val userInput = getIntegerInput()
    val clientFile = File("sessions/${sessions[userInput]}/client.ser")
    val cookieFile = File("sessions/${sessions[userInput]}/cookie.ser")
    printlnC { "${now()} ===> Login success!".green.bright }
    return IGClient.deserialize(clientFile, cookieFile)
}

private fun handleUserPostsFetcher() {
    printlnC { "Enter instagram username to see posts: ".blue.bright }
    val targetUsername = scanner.nextLine().trim()
    val getPostsLoading = loadingAsync()
    val posts = requestHelper.getUserFeed(targetUsername)
    getPostsLoading.cancel()
    printlnC { "${now()} ===> \n${posts.size}".green.bright + " posts have been fetched, enter number of posts you want to see: ".green }
    val count = getIntegerInput()
    if (count != -1) printlnC { posts.take(count).pretty().green.bright }

    printlnC { "Do you want to save posts' images as files? (y/n)".blue.bright }
    val isSavingImages = scanner.nextLine().trim().lowercase(Locale.getDefault()) == YES
    if (isSavingImages) saveImages(posts, targetUsername)
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
            val imageLoading = loadingAsync()
            val image = URL(imageUrl).readBytes()
            imageFile.writeBytes(image)
            imageLoading.cancel()
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
    printlnC { "Enter instagram username: ".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "Enter instagram password: ".blue.bright }
    val password = scanner.nextLine().trim()

    val logInLoading = loadingAsync()
    val client = LoginHelper(username, password).logInWithChallenge()
    logInLoading.cancel()
    loggerD("${now()} ===> Login success!")

    if (client.isLoggedIn) createSessionFiles(client, username)
    else println("${now()} ===> Login failed!")
    return client
}

private fun previewCurrentUserInfo() {
    val getUserInfoLoading = loadingAsync()
    val userInfo = requestHelper.getCurrentUserInfo()
    getUserInfoLoading.cancel()
    if (userInfo != null) printUserInfo(userInfo) else printlnC { "${now()} ===> User info is null".red.bold }
}

private fun printUserInfo(user: User) {
    printlnC { "${now()} ===> Full name: ${user.full_name}".green.bright }
    printlnC { "${now()} ===> Username: ${user.username}".green.bright }
    printlnC { "${now()} ===> Profile Url: ${"https://instagram.com/${user.username}"}".green.bright }
    printlnC { "${now()} ===> Profile Pic Url: ${user.profile_pic_url}".green.bright }
    printlnC { "${now()} ===> Media Count: ${user.media_count}".green.bright }
}

private fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}