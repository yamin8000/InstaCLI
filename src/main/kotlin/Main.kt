package yamin

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import com.github.instagram4j.instagram4j.models.user.User
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.printlnC
import yamin.utils.CONSTANTS.menuText
import yamin.utils.JsonUtils.pretty
import yamin.utils.LoggerHelper.loadingAsync
import yamin.utils.LoggerHelper.loggerD
import yamin.utils.LoggerHelper.loggerE
import yamin.utils.LoginHelper
import yamin.utils.RequestHelper
import java.io.File
import java.util.*

fun main() {
    loggerD("Start!") {}
    try {
        menuHandler()
    } catch (logInException: IGLoginException) {
        println("Login failed!")
        loggerE(logInException.stackTraceToString()) {}
    } catch (exception: Exception) {
        loggerD("General Error!") {}
        loggerE(exception.stackTraceToString())
    }
}

fun menuHandler() {
    var client: IGClient? = null
    val scanner = Scanner(System.`in`)

    showMenuText()
    do {
        when (getIntegerInput(scanner)) {
            0 -> showMenuText()
            1 -> client = getClientByUsernamePassword(scanner)
            2 -> {
                client = getClientBySession(scanner)
                if (client == null) printlnC { "There's no saved sessions".red }
            }
            3 -> {
                if (client != null) handleUserPostsFetcher(scanner, client)
                else printlnC { "Please login first!".red }
            }
            4 -> {
                if (client != null) handleSendingDirectMessage(client, scanner)
                else printlnC { "Please login first!".red }
            }
            5 -> {
                if (client != null) {
                    printlnC { "Enter instagram username to see friends".blue.bright }
                    val username = scanner.nextLine().trim()
                    printlnC { "Choose friends' type, Followers = 1, Followings = 2 (1/2)?" }
                    val typeInput = getIntegerInput(scanner)
                    val friendshipType =
                        if (typeInput == 1) FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
                        else FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS
                    printlnC {
                        RequestHelper(client).getUserFriends(
                            username,
                            friendshipsType = friendshipType
                        ).pretty().green.bright
                    }
                }
            }
            else -> printlnC { "Invalid menu input!".red }
        }
        continue
    } while (true)
}

private fun handleSendingDirectMessage(
    client: IGClient,
    scanner: Scanner
) {
    val helper = RequestHelper(client)
    printlnC { "Enter usernames you want to send message. (multiple usernames are seperated by -): ".blue.bright }
    val usernames = scanner.nextLine().trim().split("-")
    printlnC { "Enter message you want to send:".blue.bright }
    val message = scanner.nextLine().trim()
    if (usernames.isEmpty() || message.isBlank()) {
        printlnC { "Username or message is empty, try again!" }
        return
    } else {
        val pkLoading = CoroutineScope(Dispatchers.Default).launch { loadingAsync() }
        val pks = usernames.map { helper.getPk(it) }
        pkLoading.cancel()
        if (pks.isNotEmpty()) {
            pks.forEachIndexed { index, pk ->
                if (pk != null) {
                    val messageLoading = CoroutineScope(Dispatchers.Default).launch { loadingAsync() }
                    val isDataSent = helper.sendDirectMessageByPks(message, pk)
                    if (isDataSent) printlnC { "Message sent successfully to ".green.bright + usernames[index].blue.bright.bold }
                    messageLoading.cancel()
                }
            }
        }
    }
}

fun showMenuText() = printlnC { menuText.green.bold }

private fun getIntegerInput(scanner: Scanner): Int {
    return try {
        scanner.nextLine().trim().toInt()
    } catch (exception: NumberFormatException) {
        -1
    }
}

fun getClientBySession(scanner: Scanner): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    printlnC { "Choose your account: ".blue.bright }
    sessions.forEachIndexed { index, name -> println("$index. $name") }
    val userInput = getIntegerInput(scanner)
    val clientFile = File("sessions/${sessions[userInput]}/client.ser")
    val cookieFile = File("sessions/${sessions[userInput]}/cookie.ser")
    printlnC { "Login success!".green.bright }
    val client = IGClient.deserialize(clientFile, cookieFile)
    previewUserInfo(client)
    return client
}

private fun handleUserPostsFetcher(scanner: Scanner, client: IGClient) {
    printlnC { "Enter instagram username to see posts: ".blue.bright }
    val targetUsername = scanner.nextLine().trim()
    val getPostsLoading = CoroutineScope(Dispatchers.Default).launch { loadingAsync() }
    val posts = RequestHelper(client).getUserFeed(targetUsername)
    getPostsLoading.cancel()
    printlnC { "\n${posts.size}".green.bright + " posts have been fetched, enter number of posts you want to see: ".green }
    val count = getIntegerInput(scanner)
    if (count != -1) printlnC { posts.take(count).pretty().green.bright }
}

private fun getClientByUsernamePassword(scanner: Scanner): IGClient {
    printlnC { "Enter instagram username: ".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "Enter instagram password: ".blue.bright }
    val password = scanner.nextLine().trim()

    val logInLoading = CoroutineScope(Dispatchers.Default).launch { loadingAsync() }
    val client = LoginHelper(username, password).logInWithChallenge()
    logInLoading.cancel()
    loggerD("Login success!")

    if (client.isLoggedIn) {
        previewUserInfo(client)
        createSessionFiles(client, username)
    } else println("Login failed!")
    return client
}

private fun previewUserInfo(client: IGClient) {
    val getUserInfoLoading = CoroutineScope(Dispatchers.Default).launch { loadingAsync() }
    client.sendRequest(AccountsCurrentUserRequest()).handle { response, throwable ->
        if (response != null && throwable == null) {
            val currentUser = response.user
            if (currentUser != null) printUserInfo(currentUser)
            else loggerE("User info is null")
        } else loggerD(throwable.stackTraceToString())
        getUserInfoLoading.cancel()
    }
}

private fun printUserInfo(currentUser: User) {
    println("Current user full name is: ${currentUser.full_name} and username is: ${currentUser.username}")
}

fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}