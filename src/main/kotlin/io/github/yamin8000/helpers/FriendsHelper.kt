package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Constants.MAX_COUNT
import io.github.yamin8000.utils.Constants.PAGE_LIMIT
import io.github.yamin8000.utils.Constants.sleepDelay
import io.github.yamin8000.utils.Utility.pair
import io.github.yamin8000.utils.Utility.requirePositiveLimit

class FriendsHelper(private val igClient: IGClient) {

    fun getFollowers(
        username: String,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, limit, nextMaxId)

    fun getFollowing(
        username: String,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING, limit, nextMaxId)

    fun getFriends(
        username: String,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
    ): Dyad<List<Profile>?> {
        val (pk, pkError) = UserHelper(igClient).getPk(username)
        return if (pk != null && pkError == null) getFriends(pk, friendsType, limit, nextMaxId)
        else return null to pkError
    }

    private fun getFriends(
        pk: Long,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
    ): Dyad<List<Profile>?> {
        requirePositiveLimit(limit)
        val friends: MutableList<Profile>?
        val request = FriendshipsFeedsRequest(pk, friendsType, nextMaxId)
        val (friendsResponse, friendsError) = igClient.sendRequest(request).pair()
        if (friendsResponse != null && friendsError == null) {
            friends = mutableListOf()
            friends.addAll(friendsResponse.users.take(limit))
            when {
                friends.size == limit -> return friends to null
                friendsResponse.isMore_available -> {
                    Thread.sleep(sleepDelay)
                    val moreFriends = getFriends(
                        pk,
                        friendsType,
                        limit - friends.size,
                        friendsResponse.next_max_id,
                    )
                    friends.addAll(moreFriends.first ?: listOf())
                }
            }
        } else return null to friendsError
        return friends to null
    }

    fun getFriendsPaged(
        username: String,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        nextMaxId: String? = null,
    ): Pair<Dyad<List<Profile?>?>, String?> {
        val (pk, pkError) = UserHelper(igClient).getPk(username)
        return if (pk != null && pkError == null) getFriendsPaged(pk, friendsType, nextMaxId)
        else return (null to pkError) to null
    }

    private fun getFriendsPaged(
        pk: Long,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        nextMaxId: String? = null,
    ): Pair<Dyad<List<Profile?>?>, String?> {
        val friends: MutableList<Profile>?
        val request = FriendshipsFeedsRequest(pk, friendsType, nextMaxId)
        val (friendsResponse, friendsError) = igClient.sendRequest(request).pair()
        if (friendsResponse != null && friendsError == null) {
            friends = mutableListOf()
            friends.addAll(friendsResponse.users.take(PAGE_LIMIT))
            if (friends.size <= PAGE_LIMIT) return (friends to null) to friendsResponse.next_max_id
            else if (friendsResponse.isMore_available) {
                Thread.sleep(sleepDelay)
                val moreFriends = getFriends(
                    pk,
                    friendsType,
                    PAGE_LIMIT,
                    friendsResponse.next_max_id,
                )
                friends.addAll(moreFriends.first ?: listOf())
            }
        } else return (null to friendsError) to null
        return (friends to null) to friendsResponse.next_max_id
    }
}