package io.github.instakiller.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.Comment
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.live.*
import com.github.instagram4j.instagram4j.responses.IGResponse
import com.github.instagram4j.instagram4j.responses.live.LiveBroadcastCommentResponse
import com.github.instagram4j.instagram4j.responses.live.LiveCreateResponse
import com.github.instagram4j.instagram4j.responses.live.LiveStartResponse
import io.github.instakiller.Dyad
import io.github.instakiller.utils.Utility.pair

class LiveStreamHelper(private val igClient: IGClient) {

    fun createLive(): Pair<LiveCreateResponse?, Throwable?> {
        return LiveCreateRequest().execute(igClient).pair()
    }

    fun startLive(
        broadcastId: String,
        sendNotification: Boolean = false
    ): Dyad<LiveStartResponse?> {
        return LiveStartRequest(broadcastId, sendNotification)
            .execute(igClient)
            .pair()
    }

    fun stopLive(broadcastId: String): Dyad<IGResponse?> {
        return LiveEndBroadcastRequest(broadcastId, true)
            .execute(igClient)
            .pair()
    }

    fun getLiveInfo(broadcastId: String): Dyad<IGResponse?> {
        return LiveBroadcastInfoRequest(broadcastId)
            .execute(igClient)
            .pair()
    }

    fun getViewerList(broadcastId: String): Dyad<MutableList<Profile>?> {
        val (response, error) = LiveBroadcastGetViewerListRequest(broadcastId)
            .execute(igClient)
            .pair()
        return if (response != null && error == null) response.users to null
        else null to error
    }

    fun addComment(
        broadcastId: String,
        comment: String
    ): Dyad<LiveBroadcastCommentResponse?> {
        return LiveBroadcastCommentRequest(broadcastId, comment)
            .execute(igClient)
            .pair()
    }

    fun getComments(broadcastId: String): Dyad<MutableList<Comment>?> {
        val (response, error) = LiveBroadcastGetCommentRequest(broadcastId)
            .execute(igClient)
            .pair()
        return if (response != null && error == null) response.comments to null else null to error
    }
}