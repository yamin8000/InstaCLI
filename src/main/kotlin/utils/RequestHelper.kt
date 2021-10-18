package yamin.utils

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest

const val OK = "ok"
const val POST_LIMIT_COUNT = 50

/**
 * @param igClient instagram client used for sending request using that user
 */
class RequestHelper(private val igClient: IGClient) {

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
                    val nextData = getUserFeed(username, response.next_max_id, limit - posts.size)
                    posts.addAll(nextData)
                }
            }
        }
        return posts
    }

    private fun getPk(username: String): Long? {
        val response = igClient.sendRequest(UsersUsernameInfoRequest(username)).join()
        if (response.status == OK) return response.user.pk
        return null
    }
}