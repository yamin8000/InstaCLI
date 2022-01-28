package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Constants.MAX_COUNT
import io.github.yamin8000.utils.Constants.sleepDelay
import io.github.yamin8000.utils.Utility.limitMustNotBeNegative
import io.github.yamin8000.utils.Utility.pair

class FriendsHelper(private val igClient: IGClient) {

    fun getFollowers(
        username: String,
        limit: Int = MAX_COUNT,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, limit, null)

    fun getFollowing(
        username: String,
        limit: Int = MAX_COUNT,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING, limit, null)

    fun getFriends(
        username: String,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
    ): Dyad<List<Profile>?> {
        limitMustNotBeNegative(limit)
        val friends: MutableList<Profile>?

        val (pk, pkError) = UserHelper(igClient).getPk(username)
        if (pk != null && pkError == null) {
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
                            username,
                            friendsType,
                            limit - friends.size,
                            friendsResponse.next_max_id,
                        )
                        friends.addAll(moreFriends.first ?: listOf())
                    }
                }
            } else return null to friendsError
        } else return null to pkError
        return friends to null
    }
}