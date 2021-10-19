package yamin.utils

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest
import yamin.utils.CONSTANTS.OK
import yamin.utils.CONSTANTS.POST_LIMIT_COUNT
import yamin.utils.LoggerHelper.loggerD

/**
 * @param igClient instagram client used for sending request using that user
 */
class RequestHelper(private val igClient: IGClient) {

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
        nextMaxId: String? = null,
        limit: Int = POST_LIMIT_COUNT
    ): MutableList<TimelineMedia> {
        val posts = mutableListOf<TimelineMedia>()
        val pk = getPk(username)
        if (pk != null) {
            val response = igClient.sendRequest(FeedUserRequest(pk, nextMaxId)).join()
            if (response.status == OK) {
                posts.addAll(response.items.take(limit - posts.size))
                if (posts.size == limit) return posts
                if (response.isMore_available) {
                    val morePosts = getUserFeed(username, response.next_max_id, limit - posts.size)
                    posts.addAll(morePosts)
                }
            }
        } else loggerD("pk is null!")
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

//DirectThreadsRequest(firstThread.thread_id)
//println(client.isLoggedIn)

//    var usersSearchResponse = client.sendRequest(UsersSearchRequest("therock")).join()
//    usersSearchResponse.users.forEach { user ->
//        println(user.pk)
//    }