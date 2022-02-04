package io.github.yamin8000.modules

import com.github.ajalt.mordant.rendering.BorderStyle
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.table
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.direct.IGThread
import com.github.instagram4j.instagram4j.models.direct.item.ThreadTextItem
import io.github.yamin8000.console.ConsoleHelper.getMultipleStrings
import io.github.yamin8000.console.ConsoleHelper.getSingleString
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.DirectMessageHelper
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.helpers.UserHelper
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.menuStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import io.github.yamin8000.utils.Menus.directMessageMenu
import java.util.*

class DirectMessageModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, directMessageMenu) {

    private val helper: DirectMessageHelper by lazy(LazyThreadSafetyMode.NONE) {
        DirectMessageHelper(
            igClient
        )
    }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> getDirectMessages()
            3 -> sendDirectMessage()
            else -> {
                ter.println(errorStyle("Invalid input, try again!"))
                run()
            }
        }

        run()
        return 0
    }

    private fun sendDirectMessage() {
        val usernames = scanner.getMultipleStrings("username")
        ter.println(TextColors.blue("Enter message you want to send:"))
        val message = scanner.getSingleString("message")
        val userHelper = UserHelper(igClient)
        usernames.forEach { username ->
            val (pk, error) = userHelper.getPk(username)
            if (pk != null && error == null) sendSingleDirectMessage(message, pk, username)
            else ter.println(errorStyle("Skipping, Failed to get pk of $username! Error: ${error?.message}"))
        }
    }

    private fun sendSingleDirectMessage(message: String, pk: Long, username: String) {
        progress {
            val (isSent, error) = helper.sendDirectMessageByPks(message, pk)
            it()
            if (isSent != null && isSent && error == null)
                ter.println(resultStyle("Message successfully sent to ") + TextColors.blue(username))
            else ter.println(errorStyle("Skipping, Failed to send message to ($username) => ${error?.message}"))
        }
    }

    private fun getDirectMessages() {
        loading {
            val (inbox, error) = helper.getInbox()
            it()
            if (inbox != null && error == null) {
                ter.println(resultStyle("Unread count: ${inbox.unseen_count}"))
                val threads = inbox.threads
                if (threads != null) {
                    if (threads.isNotEmpty()) printThreads(threads)
                    else ter.println(warningStyle("No direct messages found!"))
                } else ter.println(errorStyle("Failed to get threads!"))
            } else ter.println(errorStyle("Failed to get inbox! Error: ${error?.message}"))
        }
    }

    private fun printThreads(threads: List<IGThread>) {
        threads.forEach {
            printSingleThread(it)
            scanner.pressEnterToContinue("see next direct message")
        }
        ter.println(resultStyle("All direct messages shown!"))
    }

    private fun printSingleThread(thread: IGThread) {
        ter.println(
            table {
                borderTextStyle = TextColors.brightBlue
                borderStyle = BorderStyle.ROUNDED
                body {
                    style = menuStyle
                    row("Sender", thread.thread_title)
                    row("Users", thread.users.joinToString(", ") { user -> user.username })
                    val lastItem = thread.last_permanent_item
                    row("Item", if (lastItem is ThreadTextItem) lastItem.text else "Unknown")
                }
            }
        )
    }
}