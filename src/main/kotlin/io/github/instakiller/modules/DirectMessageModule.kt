package io.github.instakiller.modules

import com.github.ajalt.mordant.rendering.BorderType
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.table
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.direct.IGThread
import com.github.instagram4j.instagram4j.models.direct.item.ThreadTextItem
import io.github.instakiller.console.ConsoleHelper.readMultipleStrings
import io.github.instakiller.console.ConsoleHelper.readSingleString
import io.github.instakiller.console.ConsoleHelper.pressEnterToContinue
import io.github.instakiller.helpers.DirectMessageHelper
import io.github.instakiller.helpers.LoggerHelper.loading
import io.github.instakiller.helpers.LoggerHelper.progress
import io.github.instakiller.helpers.UserHelper
import io.github.instakiller.utils.Constants.askStyle
import io.github.instakiller.utils.Constants.errorStyle
import io.github.instakiller.utils.Constants.menuStyle
import io.github.instakiller.utils.Constants.resultStyle
import io.github.instakiller.utils.Constants.ter
import io.github.instakiller.utils.Constants.warningStyle
import io.github.instakiller.utils.Menus.directMessageMenu
import io.github.instakiller.utils.Utility.solo

class DirectMessageModule(private val igClient: IGClient) : BaseModule(directMessageMenu) {

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
            3 -> sendDirectMessageToUsers()
            else -> {
                ter.println(errorStyle("Invalid input, try again!"))
                run()
            }
        }

        run()
        return 0
    }

    private fun sendDirectMessageToUsers() {
        val usernames = readMultipleStrings("username")
        ter.println(TextColors.blue("Enter message you want to send:"))
        val message = readSingleString("message")
        val userHelper = UserHelper(igClient)
        usernames.forEach { username ->
            loading {
                userHelper.getPk(username).solo({
                    it()
                    sendSingleDirectMessage(message, it, username)
                }, { it() })
            }
        }
    }

    private fun sendSingleDirectMessage(message: String, pk: Long, username: String) {
        progress { stopLoading ->
            helper.sendDirectMessageByPks(message, pk).solo({ isSent ->
                stopLoading()
                if (isSent)
                    ter.println(resultStyle("Message successfully sent to ${askStyle(username)}"))
            }, { stopLoading() })
        }
    }

    private fun getDirectMessages() {
        loading { stopLoading ->
            helper.getInbox().solo({ inbox ->
                stopLoading()
                ter.println(resultStyle("Unread count: ${inbox.unseen_count}"))
                val threads = inbox.threads
                if (threads != null) {
                    if (threads.isNotEmpty()) printThreads(threads)
                    else ter.println(warningStyle("No direct messages found!"))
                } else ter.println(errorStyle("Failed to get threads!"))
            }, { stopLoading() })
        }
    }

    private fun printThreads(threads: List<IGThread>) {
        threads.forEach {
            printSingleThread(it)
            pressEnterToContinue("see next direct message")
        }
        ter.println(resultStyle("All direct messages shown!"))
    }

    private fun printSingleThread(thread: IGThread) {
        ter.println(
            table {
                borderStyle = TextColors.brightBlue
                borderType = BorderType.ROUNDED
                body {
                    style = menuStyle
                    row("Thread title (sender name or group name)", thread.thread_title)
                    row("User/Users (usernames)", thread.users.joinToString(", ") { user -> user.username })
                    val lastItem = thread.last_permanent_item
                    row("Item", if (lastItem is ThreadTextItem) lastItem.text else "Unknown")
                }
            }
        )
    }
}