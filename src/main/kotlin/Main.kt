package yamin

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import yamin.console.ConsoleHelper.getIntegerInput
import yamin.console.printlnC
import yamin.helpers.FriendsHelper
import yamin.helpers.LoggerHelper.loggerD
import yamin.helpers.LoggerHelper.loggerE
import yamin.helpers.LoggerHelper.progress
import yamin.helpers.LoginHelper
import yamin.helpers.RequestHelper
import yamin.helpers.UserHelper
import yamin.modules.MainModule
import yamin.utils.Constants.loginMenu
import yamin.utils.Utility.now
import java.io.File
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
    MainModule(scanner, igClient).run()
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

private fun getClientByUsernamePassword(): IGClient {
    val enterField = "Enter instagram "
    printlnC { "$enterField username: ".blue.bright }
    val username = scanner.nextLine().trim()
    printlnC { "$enterField password: ".blue.bright }
    val password = scanner.nextLine().trim()

    val client = LoginHelper.logInWithChallenge(username, password)

    if (client.isLoggedIn) {
        createSessionFiles(client, username)
        printlnC { "${now()} ===> Logged in successfully as ($username)".green.bright }
    } else printlnC { "${now()} ===> Login failed!".red.bold }

    return client
}

private fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}