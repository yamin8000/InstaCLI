package yamin.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import yamin.utils.Constants.LIMIT_COUNT
import yamin.utils.Constants.sleepDelay
import yamin.utils.Utility.limitMustNotBeNegative
import yamin.utils.Utility.pair

class UserFriendsHelper(private val igClient: IGClient) {

    fun getFollowers(
        username: String,
        limit: Int = LIMIT_COUNT,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, limit, null)

    fun getFollowing(
        username: String,
        limit: Int = LIMIT_COUNT,
    ) = getFriends(username, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWING, limit, null)

    private fun getFriends(
        username: String,
        friendsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        limit: Int = LIMIT_COUNT,
        nextMaxId: String? = null,
    ): Pair<List<Profile>?, Throwable?> {
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