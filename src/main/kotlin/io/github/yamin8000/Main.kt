package io.github.yamin8000

import com.github.ajalt.mordant.rendering.BorderType
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.table
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.console.ConsoleHelper.readCleanLine
import io.github.yamin8000.console.ConsoleHelper.readInteger
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.helpers.LoggerHelper.loggerD
import io.github.yamin8000.helpers.LoggerHelper.loggerE
import io.github.yamin8000.helpers.LoginHelper
import io.github.yamin8000.models.CommandLineLogin
import io.github.yamin8000.modules.MainModule
import io.github.yamin8000.modules.SettingsModule
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.isAutosavingSession
import io.github.yamin8000.utils.Constants.menuStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import io.github.yamin8000.utils.Menus.initMenu
import io.github.yamin8000.utils.Utility
import java.io.File
import kotlin.system.exitProcess

typealias Dyad<T> = Pair<T, Throwable?>

private lateinit var igClient: IGClient

private val sessions by lazy(LazyThreadSafetyMode.NONE) { File("sessions").list() }

private var args = arrayOf<String>()

fun main(arguments: Array<String>) {
    args = arguments

    if (arguments.isEmpty()) handleRegularMode()
    else handleCommandLineOptions()
}

/**
 * Handles the regular mode of the application,
 * where the user is prompted to log in.
 */
fun handleRegularMode() {
    try {
        initLogin()
    } catch (logInException: IGLoginException) {
        ter.println(warningStyle(Utility.now()))
        ter.println(errorStyle("Login failed: ${logInException.message}"))
        initLogin()
    } catch (exception: Exception) {
        ter.println(warningStyle(Utility.now()))
        loggerD("General Error!") {}
        loggerE(exception.stackTraceToString()) {}
        return
    }
}

/**
 * Handles the command line mode of the application,
 * where log in info is passed in as arguments.
 */
fun handleCommandLineOptions() {
    try {
        val commands = filterCommands()
        if (commands.isNotEmpty()) executeCommands(commands)
        else ter.println(warningStyle("No commands found!"))
    } catch (exception: Exception) {
        ter.println(warningStyle(Utility.now()))
        loggerD("General Error!") {}
        loggerE(exception.stackTraceToString()) {}
        return
    }
}

private fun filterCommands(): List<Pair<String, Int>> {
    return args.asSequence().filter { it.startsWith("-") }
        .mapIndexed { index, command ->
            command.substring(1) to index
        }.toList()
}

private fun executeCommands(commands: List<Pair<String, Int>>) {
    for ((command, commandIndex) in commands) {
        when (command) {
            "login" -> executeLoginCommand(commandIndex)
            "session" -> loadSessionFromCommandLine(args[commandIndex + 1])
        }
        break
    }
}

private fun executeLoginCommand(commandIndex: Int) {
    val isAutosaving = parseAutosavingArg(commandIndex)
    val login = CommandLineLogin(
        args[commandIndex + 1],
        args[commandIndex + 2],
        isAutosaving
    )
    loginFromCommandLine(login)
}

private fun parseAutosavingArg(commandIndex: Int) =
    when (args.getOrNull(commandIndex + 3) ?: "autosave-on") {
        "autosave-on" -> true
        "autosave-off" -> false
        else -> throw IllegalArgumentException("Invalid autosaving argument!: ${args[commandIndex + 3]}")
    }

fun loginFromCommandLine(login: CommandLineLogin) {
    isAutosavingSession = login.isAutosaving
    val temp = loginWithUsernamePassword(login.username, login.password)

    if (temp == null) ter.println(errorStyle("Login failed!"))
    else igClient = temp

    initLogin(true)
}

fun loadSessionFromCommandLine(sessionArg: String) {
    var sessionIndex: Int? = null

    for ((index, session) in sessions.withIndex()) {
        if (session == sessionArg) sessionIndex = index
    }
    if (sessionIndex != null) {
        val temp = loginFromSession(sessionIndex)

        if (temp != null) {
            igClient = temp
            initLogin(true)
        } else ter.println(errorStyle("Session loading failed!"))
    } else ter.println(errorStyle("Session not found!"))
}

private fun initLogin(isCommandLine: Boolean = false) {
    SettingsModule()

    if (!isCommandLine)
        igClient = loginHandler() ?: exitProcess(0)
    val menu = MainModule(igClient).run()
    if (menu == 0) initLogin()
}

fun loginHandler(): IGClient? {
    ter.println(table {
        borderType = BorderType.ROUNDED
        borderStyle = TextColors.brightBlue
        header { row("${resultStyle("Welcome to")} ${TextColors.brightGreen("InstaKiller")}") }
        body {
            style = menuStyle
            initMenu.split("\n").forEach { row(it) }
        }
        captionBottom(TextColors.brightBlue("Please login first:"))
    })
    return when (readInteger()) {
        0 -> loginHandler()
        1 -> getClientByUsernamePassword() ?: loginHandler()
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

private fun getClientBySession(): IGClient? {
    if (sessions.isNullOrEmpty()) return null
    ter.println(table {
        borderStyle = TextColors.brightBlue
        borderType = BorderType.ROUNDED
        header { row(TextColors.blue("Available sessions:")) }
        body {
            style = menuStyle
            sessions.forEachIndexed { index, session -> row("$index. $session") }
        }
        captionBottom(TextColors.brightBlue("Please choose session:"))
    })
    val userInput = readInteger()
    if (userInput in sessions.indices) return loginFromSession(userInput)
    return null
}

private fun loginFromSession(sessionIndex: Int): IGClient? {
    val clientFile = File("sessions/${sessions[sessionIndex]}/client.ser")
    val cookieFile = File("sessions/${sessions[sessionIndex]}/cookie.ser")
    ter.println(TextColors.green("Login success!"))
    val client = IGClient.deserialize(clientFile, cookieFile)
    return if (client.isLoggedIn) client else null
}

private fun getClientByUsernamePassword(): IGClient? {
    val enterField = "Enter instagram "
    ter.println(TextColors.blue("$enterField username: "))
    val username = readCleanLine()
    ter.println(TextColors.blue("$enterField password: "))
    val password = readCleanLine()

    return loginWithUsernamePassword(username, password)
}

private fun loginWithUsernamePassword(
    username: String,
    password: String
): IGClient? {
    val (client, error) = loading {
        val igPair = LoginHelper().logInWithChallenge(username, password)
        it()
        return@loading igPair
    }

    if (client != null && error == null) {
        if (client.isLoggedIn) {
            if (isAutosavingSession)
                createSessionFiles(client, username)
            ter.println(resultStyle("Logged in successfully as ($username)"))
        } else {
            ter.println(errorStyle("Login failed!"))
            pressEnterToContinue()
        }
    } else {
        ter.println(errorStyle("Login failed: ${error?.message}"))
        pressEnterToContinue()
    }

    return client
}

private fun createSessionFiles(
    client: IGClient,
    username: String
) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}