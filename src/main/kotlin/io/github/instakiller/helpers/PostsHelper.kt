package io.github.instakiller.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import com.github.instagram4j.instagram4j.requests.feed.FeedUserRequest
import com.github.instagram4j.instagram4j.responses.feed.FeedUserResponse
import io.github.instakiller.Dyad
import io.github.instakiller.utils.Constants.MAX_COUNT
import io.github.instakiller.utils.Constants.sleepDelay
import io.github.instakiller.utils.Utility.pair

class PostsHelper(private val igClient: IGClient) {

    private val userHelper: UserHelper by lazy(LazyThreadSafetyMode.NONE) { UserHelper(igClient) }

    fun getUserFeed(
        username: String,
        limit: Int = MAX_COUNT,
        nextMaxId: String? = null,
        pkRecursive: Long? = null,
    ): Dyad<List<TimelineMedia>?> {
        val (rawFeeds, error) = getRawUserFeed(username, 1, nextMaxId, pkRecursive)
        return if (rawFeeds != null && error == null) {
            val feeds = mutableListOf<TimelineMedia>()
            rawFeeds.forEach {
                feeds.addAll(it.items.take(limit))
                if (feeds.size == limit) return feeds to null
                if (it.isMore_available) {
                    Thread.sleep(sleepDelay)
                    val moreFeeds = getUserFeed(username, limit - feeds.size, it.next_max_id, pkRecursive)
                    feeds.addAll(moreFeeds.first ?: listOf())
                }
            }
            feeds to null
        } else null to error
    }

    private fun getRawUserFeed(
        username: String,
        pages: Int = 1,
        nextMaxId: String? = null,
        pkRecursive: Long? = null,
    ): Dyad<List<FeedUserResponse>?> {
        if (pkRecursive == null) {
            val feeds = mutableListOf<FeedUserResponse>()
            val (pk, pkError) = userHelper.getPk(username)
            return if (pk != null && pkError == null) {
                val (feedResponse, feedError) = igClient.sendRequest(FeedUserRequest(pk, nextMaxId)).pair()
                if (feedResponse != null && feedError == null) {
                    feeds.add(feedResponse)
                    when {
                        feeds.size == pages -> feeds to null
                        feedResponse.isMore_available -> {
                            Thread.sleep(sleepDelay)
                            val (moreFeeds, _) = getRawUserFeed(username, pages, feedResponse.next_max_id, pk)
                            feeds.addAll(moreFeeds ?: listOf())
                            feeds to null
                        }
                        else -> feeds to null
                    }
                } else null to feedError
            } else null to pkError
        } else return getRawUserFeed(username, pages, nextMaxId, pkRecursive)
    }
}