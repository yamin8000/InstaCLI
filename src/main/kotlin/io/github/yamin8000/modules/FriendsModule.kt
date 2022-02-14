package io.github.yamin8000.modules

import com.github.ajalt.mordant.rendering.TextColors
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import io.github.yamin8000.console.ConsoleHelper.getBooleanInput
import io.github.yamin8000.console.ConsoleHelper.getSingleString
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.FriendsHelper
import io.github.yamin8000.helpers.LoggerHelper.loading
import io.github.yamin8000.helpers.LoggerHelper.progress
import io.github.yamin8000.helpers.UserHelper
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.infoStyle
import io.github.yamin8000.utils.Constants.resultStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import io.github.yamin8000.utils.Menus.friendsMenu
import io.github.yamin8000.utils.Utility.solo
import java.util.*

class FriendsModule(scanner: Scanner, private val igClient: IGClient) : BaseModule(scanner, friendsMenu) {

    private val friendsHelper by lazy(LazyThreadSafetyMode.NONE) { FriendsHelper(igClient) }

    private val userHelper by lazy(LazyThreadSafetyMode.NONE) { UserHelper(igClient) }

    private val currentUser by lazy(LazyThreadSafetyMode.NONE) { userHelper.getCurrentUserInfo() }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showCurrentUserFollowers()
            3 -> showCurrentUserFollowing()
            4 -> showCurrentUserUnfollowers()
            5 -> showUserFollowers()
            6 -> showUserFollowing()
            7 -> showUserUnfollowers()
            else -> {
                ter.println(errorStyle("Invalid input, try again!"))
                run()
            }
        }

        run()
        return 0
    }

    private fun showCurrentUserUnfollowers() {
        loading {
            currentUser.solo({ user ->
                it()
                printUnfollowers(getUserUnfollowers(user.username), user.username)
            }, { it() })
        }
    }

    private fun showUserUnfollowers() {
        val username = scanner.getSingleString("username")
        printUnfollowers(getUserUnfollowers(username), username)
    }

    private fun printUnfollowers(
        userUnfollowers: Pair<List<Profile>, List<Profile>>,
        username: String
    ) {
        ter.println(infoStyle("People ($username) follow but they don't follow ($username) back"))
        if (userUnfollowers.first.isEmpty())
            ter.println(resultStyle("Nobody found that ($username) follow but they doesn't follow ($username) back"))
        userUnfollowers.first.forEachIndexed { index, it ->
            ter.println(resultStyle("${index + 1}. ${it.username} (${it.full_name})"))
        }
        scanner.pressEnterToContinue()
        ter.println(infoStyle("People who follow ($username) but ($username) doesn't follow them back"))
        if (userUnfollowers.second.isEmpty())
            ter.println(resultStyle("Nobody that follow ($username) but ($username) doesn't follow them back"))
        userUnfollowers.second.forEachIndexed { index, it ->
            ter.println(resultStyle("${index + 1}. ${it.username} (${it.full_name})"))
        }
        scanner.pressEnterToContinue()
    }

    private fun getUserUnfollowers(username: String): Pair<List<Profile>, List<Profile>> {
        ter.println(warningStyle("This is a very heavy operation, please be patient!"))
        val followers = getUserAllFollowers(username)
        val following = getUserAllFollowings(username)
        return following.filter { !followers.contains(it) } to followers.filter { !following.contains(it) }
    }

    private fun getUserAllFollowers(username: String) = loading {
        val (response, error) = friendsHelper.getFollowers(username)
        it()
        if (response != null && error == null) return@loading response else return@loading listOf()
    }

    private fun getUserAllFollowings(username: String) = loading {
        val (response, error) = friendsHelper.getFollowing(username)
        it()
        if (response != null && error == null) return@loading response else return@loading listOf()
    }

    private fun showUserFollowing(input: String? = null) {
        val username = input ?: scanner.getSingleString("username")
        showFollowingCount(username)
        showFriendsPaged(FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING, username)
        scanner.pressEnterToContinue()
    }

    private fun showFollowingCount(username: String) {
        loading {
            userHelper.getUserInfoByUsername(username).solo({ user ->
                it()
                ter.println(resultStyle("Total followings of ($username) is: ${user.following_count}"))
            }, { it() })
        }
    }

    private fun showUserFollowers(input: String? = null) {
        val username = input ?: scanner.getSingleString("username")
        showFollowersCount(username)
        showFriendsPaged(FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, username)
        scanner.pressEnterToContinue()
    }

    private fun showFollowersCount(username: String) {
        loading {
            userHelper.getUserInfoByUsername(username).solo({ user ->
                it()
                ter.println(resultStyle("Total followers of ($username) is: ${user.follower_count}"))
            }, { it() })
        }
    }

    private fun showCurrentUserFollowing() {
        loading { stopLoading ->
            currentUser.solo({
                stopLoading()
                showUserFollowing(it.username)
            }, { stopLoading() })
        }
    }

    private fun showCurrentUserFollowers() {
        loading { stopLoading ->
            currentUser.solo({
                stopLoading()
                showUserFollowers(it.username)
            }, { stopLoading() })
        }
    }

    private fun showFriendsPaged(
        friendType: FriendshipsFeedsRequest.FriendshipsFeeds,
        username: String,
        nextMaxId: String? = null
    ) {
        progress {
            val (result, nextId) = friendsHelper.getFriendsPaged(username, friendType, nextMaxId)
            it()

            result.solo({ followers ->
                if (followers.isNotEmpty()) {
                    ter.println(TextColors.blue("Friends:"))
                    followers.forEachIndexed { index, profile ->
                        ter.println(TextColors.blue("${index + 1}. ${profile?.username} => ${profile?.full_name}"))
                    }
                    checkIfMoreNeeded(friendType, username, nextId)
                } else ter.println(errorStyle("No friends found!"))
            })
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
            else ter.println(warningStyle("End of ($username) friends list"))
        } else ter.println(warningStyle("End of ($username) friends list"))
    }
}