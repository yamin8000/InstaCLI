package yamin.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest
import utils.printlnC
import yamin.utils.Constants.MAX_COUNT
import yamin.utils.Constants.sleepDelay
import yamin.utils.Utility.pair

class UserPostsHelper(private val igClient: IGClient) {

    fun getUserFeed(
        username: String,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
        pkRecursive: Long? = null,
    ): Pair<List<TimelineMedia>?, Throwable?> {
        val pk = pkRecursive ?: getPk(username)
        val feeds = mutableListOf<TimelineMedia>()

        if (pk != null) {
            val (feedResponse, feedError) = igClient.sendRequest(FeedUserRequest(pk, nextMaxId)).pair()
            if (feedResponse != null && feedError == null) {
                feeds.addAll(feedResponse.items.take(limit))
                when {
                    feeds.size == limit -> return feeds to null
                    feedResponse.isMore_available -> {
                        Thread.sleep(sleepDelay)
                        val moreFeeds = getUserFeed(username, limit - feeds.size, feedResponse.next_max_id, pk)
                        feeds.addAll(moreFeeds.first ?: listOf())
                    }
                }
            } else return null to feedError
        } else null to IllegalStateException("No user found with following username: $username")
        return feeds to null
    }

    private fun getPk(username: String): Long? {
        val (pk, pkError) = UserHelper(igClient).getPk(username)
        return if (pk != null && pkError == null) pk
        else {
            printlnC { "Error getting pk for $username".red.bold }
            null
        }
    }
}