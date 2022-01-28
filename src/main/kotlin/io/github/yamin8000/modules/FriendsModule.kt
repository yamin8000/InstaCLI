package io.github.yamin8000.modules

import com.github.ajalt.mordant.rendering.TextColors
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import io.github.yamin8000.console.ConsoleHelper.getBooleanInput
import io.github.yamin8000.console.ConsoleHelper.getSingleString
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.FriendsHelper
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Menus.friendsMenu
import java.util.*

class FriendsModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, friendsMenu) {

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showCurrentFollowers()
            3 -> showCurrentFollowing()
            4 -> showUserFollowers()
            5 -> showUserFollowing()
            else -> {
                ter.println(errorStyle("Invalid input, try again!"))
                run()
            }
        }

        run()
        return 0
    }

    private fun showUserFollowing() {
        showFriendsPaged(FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING, scanner.getSingleString("username"))
        scanner.pressEnterToContinue()
    }

    private fun showUserFollowers() {
        showFriendsPaged(FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, scanner.getSingleString("username"))
        scanner.pressEnterToContinue()
    }

    private fun showCurrentFollowing() {
        TODO("Not yet implemented")
    }

    private fun showCurrentFollowers() {
        TODO("Not yet implemented")
    }

    private fun showFriendsPaged(
        friendType: FriendshipsFeedsRequest.FriendshipsFeeds,
        username: String,
        nextMaxId: String? = null
    ) {
        progress {
            val friendsHelper = FriendsHelper(igClient)
            val (result, nextId) = friendsHelper.getFriendsPaged(username, friendType, nextMaxId)
            it()
            val (followers, error) = result

            if (followers != null && error == null) {
                if (followers.isNotEmpty()) {
                    ter.println(TextColors.blue("Friends:"))
                    followers.forEachIndexed { index, profile -> ter.println(TextColors.blue("${index + 1}. ${profile?.username} => ${profile?.full_name}")) }
                    checkIfMoreNeeded(friendType, username, nextId)
                } else ter.println(errorStyle("No friends found!"))
            } else ter.println(errorStyle("Failed to get friends! Error: ${error?.message}"))
        }
    }

    private fun checkIfMoreNeeded(
        friendType: FriendshipsFeedsRequest.FriendshipsFeeds,
        username: String,
        nextMaxId: String? = null
    ) {
        if (nextMaxId != null) {
            val seeMore = scanner.getBooleanInput("Do you want to see more friends? (y/n)")
            if (seeMore) showFriendsPaged(friendType, username, nextMaxId)
            else ter.println(TextColors.yellow("End of ($username) friends list"))
        } else ter.println(TextColors.yellow("End of ($username) friends list"))
    }
}