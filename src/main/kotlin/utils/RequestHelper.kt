package yamin.utils

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest
import yamin.utils.CONSTANTS.OK
import yamin.utils.CONSTANTS.POST_LIMIT_COUNT
import yamin.utils.LoggerHelper.loggerD

/**
 * @param igClient instagram client used for sending request using that user
 */
class RequestHelper(private val igClient: IGClient) {

    //duplicate code
    fun getUserFriends(
        username: String,
        pkRecursive: Long? = null,
        friendshipsType: FriendshipsFeedsRequest.FriendshipsFeeds,
        nextMaxId: String? = null,
        limit: Int = POST_LIMIT_COUNT
    ): MutableList<Profile> {
        val friends = mutableListOf<Profile>()
        val userId = pkRecursive ?: getPk(username)
        if (userId != null) {
            val request = FriendshipsFeedsRequest(userId, friendshipsType, nextMaxId)
            val response = igClient.sendRequest(request).join()
            if (response.status == OK) {
                friends.addAll(response.users.take(limit - friends.size))
                if (friends.size == limit) return friends
                if (response.isMore_available) {
                    Thread.sleep(500)
                    val moreFriends = getUserFriends(username, userId, friendshipsType, nextMaxId, limit - friends.size)
                    friends.addAll(moreFriends)
                }
            }
        } else loggerD("use pk/id is null!")
        return friends
    }

    fun sendDirectMessageByPks(message: String, vararg pkS: Long): Boolean {
        val response = igClient.sendRequest(
            DirectThreadsBroadcastRequest(
                DirectThreadsBroadcastRequest.BroadcastTextPayload(message, *pkS)
            )
        ).join()
        return response.status == OK
    }

    /**
     * get list of user posts,
     *
     * this method may throw exception
     * @param username instagram username
     * @param nextMaxId indicator/id for starting point of next page of the data, used for pagination, default is null
     * @param limit used for limiting number of posts
     * @see LoginHelper
     * @see IGClient
     */
    fun getUserFeed(
        username: String,
        pkRecursive: Long? = null,
        nextMaxId: String? = null,
        limit: Int = POST_LIMIT_COUNT
    ): MutableList<TimelineMedia> {
        val posts = mutableListOf<TimelineMedia>()
        val pk = pkRecursive ?: getPk(username)
        if (pk != null) {
            val response = igClient.sendRequest(FeedUserRequest(pk, nextMaxId)).join()
            if (response.status == OK) {
                posts.addAll(response.items.take(limit - posts.size))
                if (posts.size == limit) return posts
                if (response.isMore_available) {
                    Thread.sleep(500)
                    val morePosts = getUserFeed(username, pk, response.next_max_id, limit - posts.size)
                    posts.addAll(morePosts)
                }
            }
        } else loggerD("user pk/id is null!")
        return posts
    }

    /**
     * get pk -> primary key -> id of user based on instagram username
     */
    fun getPk(username: String): Long? {
        val response = igClient.sendRequest(UsersUsernameInfoRequest(username)).join()
        if (response.status == OK) return response.user.pk
        return null
    }
}

//    try {
//        client.actions().timeline().uploadPhoto(File("clean.jpg"), "asdasda")
//            .thenAccept { println("hello") }.join()
//    } catch (exception: Exception) {
//        println(exception.stackTraceToString())
//    }


//val data = client.sendRequest(FeedUserRequest(45250133777)).join()
//val firstId = data.items.first().id
//val likers = client.sendRequest(MediaGetLikersRequest(firstId)).join()
//println(likers.users.pretty())
//val commenters = client.sendRequest(MediaGetCommentsRequest(firstId)).join()
//println(commenters.comments.pretty())
//val count = client.sendRequest(UsersInfoRequest(7995957172)).join().user.follower_count
//println("count $count")
//    val myFollowers = mutableListOf<Profile>()
//    var nextMaxId: String? = ""
//    while (nextMaxId != null) {
//        val friendshipsFeedsRequest =
//            FriendshipsFeedsRequest(7995957172, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, nextMaxId)
//        val feedUsersResponse = client.sendRequest(friendshipsFeedsRequest).join()
//        nextMaxId = feedUsersResponse.next_max_id
//        //println(followers.users.first())
//        myFollowers.addAll(feedUsersResponse.users)
//        Thread.sleep(1000)
//    }
//    println(myFollowers.pretty())
//val usernames = myFollowers.map { it.username }
//val uniqueUsernames = mutableListOf<String>()
//usernames.forEach { if (!uniqueUsernames.contains(it)) uniqueUsernames.add(it) }
//println(uniqueUsernames.size)
//println(usernames)

//    var usersSearchResponse = client.sendRequest(UsersSearchRequest("therock")).join()
//    usersSearchResponse.users.forEach { user ->
//        println(user.pk)
//    }