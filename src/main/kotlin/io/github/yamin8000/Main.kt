package io.github.yamin8000

import com.github.ajalt.mordant.rendering.BorderStyle
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.table
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.exceptions.IGLoginException
import io.github.yamin8000.console.ConsoleHelper.getIntegerInput
import io.github.yamin8000.helpers.LoggerHelper.loggerD
import io.github.yamin8000.helpers.LoggerHelper.loggerE
import io.github.yamin8000.helpers.LoginHelper
import io.github.yamin8000.modules.MainModule
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.menuStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Menus.initMenu
import java.io.File
import java.util.*
import kotlin.system.exitProcess

typealias Dyad<T> = Pair<T, Throwable?>

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
    val menu = MainModule(scanner, igClient).run()
    if (menu == 0) initLogin()
}

fun loginHandler(): IGClient? {
    ter.println(table {
        borderStyle = BorderStyle.ROUNDED
        borderTextStyle = TextColors.brightBlue
        header { row("${resultStyle("Welcome to")} ${TextColors.brightGreen("InstaKiller")}") }
        body {
            style = menuStyle
            initMenu.split("\n").forEach { row(it) }
        }
        captionBottom(TextColors.brightBlue("Please login first:"))
    })
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

private fun getClientBySession(): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    ter.println(table {
        borderTextStyle = TextColors.brightBlue
        borderStyle = BorderStyle.ROUNDED
        header { row(TextColors.blue("Available sessions:")) }
        body {
            style = menuStyle
            sessions.forEachIndexed { index, session -> row("$index. $session") }
        }
        captionBottom(TextColors.brightBlue("Please choose session:"))
    })
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
        ter.println(resultStyle("Logged in successfully as ($username)"))
    } else ter.println(errorStyle("Login failed!"))

    return client
}

private fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}