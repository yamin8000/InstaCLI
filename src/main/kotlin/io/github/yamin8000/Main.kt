package io.github.yamin8000

import com.github.ajalt.mordant.rendering.TextColors
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import io.github.yamin8000.console.ConsoleHelper.getIntegerInput
import io.github.yamin8000.helpers.FriendsHelper
import io.github.yamin8000.helpers.LoggerHelper.loggerD
import io.github.yamin8000.helpers.LoggerHelper.loggerE
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.helpers.LoginHelper
import io.github.yamin8000.helpers.RequestHelper
import io.github.yamin8000.helpers.UserHelper
import io.github.yamin8000.modules.MainModule
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.loginMenu
import io.github.yamin8000.utils.Constants.ter
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
        ter.println(errorStyle("Login failed: ${logInException.message}"))
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
    ter.println("${TextColors.green("Welcome to")} ${TextColors.brightGreen("InstaKiller")}")
    ter.println(TextColors.cyan(loginMenu))
    ter.println(TextColors.blue("Please login first:"))
    return when (scanner.getIntegerInput()) {
        0 -> loginHandler()
        1 -> getClientByUsernamePassword()
        2 -> getClientBySession() ?: loginHandler()
        3 -> {
            ter.println(errorStyle("Bye!"))
            null
        }
        else -> {
            ter.println(errorStyle("Invalid input! Please try again."))
            loginHandler()
        }
    }
}

private fun handleGetFriends() {
    ter.println(TextColors.blue("Enter instagram username to see friends"))
    val username = scanner.nextLine().trim()
    val typeInput = scanner.getIntegerInput("Choose friends' type, Followers = 1, Followings = 2 (1/2)?")
    val limit = scanner.getIntegerInput("Enter the number of friends you want to see (default is all available)?")
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
                ter.println(TextColors.blue("Friends:"))
                followers.forEachIndexed { index, profile -> ter.println(TextColors.blue("${index + 1}. ${profile.username} => ${profile.full_name}")) }
            } else ter.println(errorStyle("No friends found!"))
        } else ter.println(errorStyle("Failed to get friends! Error: ${error?.message}"))
    }
}

private fun handleSendingDirectMessage() {
    ter.println(TextColors.blue("Enter usernames you want to send message. (multiple usernames are seperated by comma (,)): "))
    val usernames = scanner.nextLine().trim().split(",")
    ter.println(TextColors.blue("Enter message you want to send:"))
    val message = scanner.nextLine().trim()
    if (message.isNotBlank()) {
        val userHelper = UserHelper(igClient)
        usernames.forEach { username ->
            val (pk, error) = userHelper.getPk(username)
            if (pk != null && error == null) sendSingleDirectMessage(message, pk, username)
            else ter.println(errorStyle("Skipping, Failed to get pk of $username! Error: ${error?.message}"))
        }

    } else {
        ter.println(errorStyle("Message is empty, try again!"))
        //todo checking if this `return` is necessary
        return
    }
}

private fun sendSingleDirectMessage(message: String, pk: Long, username: String) {
    //val messageLoading = loadingAsync()
    val isDataSent = requestHelper.sendDirectMessageByPks(message, pk)
    if (isDataSent) ter.println(TextColors.green("Message successfully sent to ") + TextColors.blue(username))
    //messageLoading.cancel()
}

private fun getClientBySession(): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    ter.print(TextColors.blue("Choose your account: "))
    sessions.forEachIndexed { index, name -> println("$index. $name") }
    val userInput = scanner.getIntegerInput()
    if (userInput in sessions.indices) {
        val clientFile = File("sessions/${sessions[userInput]}/client.ser")
        val cookieFile = File("sessions/${sessions[userInput]}/cookie.ser")
        ter.println(TextColors.green("Login success!"))
        return IGClient.deserialize(clientFile, cookieFile)
    }
    return null
}

private fun getClientByUsernamePassword(): IGClient {
    val enterField = "Enter instagram "
    ter.println(TextColors.blue("$enterField username: "))
    val username = scanner.nextLine().trim()
    ter.println(TextColors.blue("$enterField password: "))
    val password = scanner.nextLine().trim()

    val client = LoginHelper.logInWithChallenge(username, password)

    if (client.isLoggedIn) {
        createSessionFiles(client, username)
        ter.println(TextColors.green("Logged in successfully as ($username)"))
    } else ter.println(errorStyle("Login failed!"))

    return client
}

private fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}