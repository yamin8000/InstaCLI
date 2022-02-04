package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.requests.live.LiveBroadcastInfoRequest
import com.github.instagram4j.instagram4j.requests.live.LiveCreateRequest
import com.github.instagram4j.instagram4j.requests.live.LiveEndBroadcastRequest
import com.github.instagram4j.instagram4j.requests.live.LiveStartRequest
import com.github.instagram4j.instagram4j.responses.IGResponse
import com.github.instagram4j.instagram4j.responses.live.LiveCreateResponse
import com.github.instagram4j.instagram4j.responses.live.LiveStartResponse
import io.github.yamin8000.utils.Utility.pair

class LiveStreamHelper(private val igClient: IGClient) {

    fun createLive(): Pair<LiveCreateResponse?, Throwable?> {
        return LiveCreateRequest().execute(igClient).pair()
    }

    fun startLive(
        broadcastId: String,
        sendNotification: Boolean = false
    ): Pair<LiveStartResponse?, Throwable?> {
        return LiveStartRequest(broadcastId, sendNotification)
            .execute(igClient)
            .pair()
    }

    fun stopLive(broadcastId: String): Pair<IGResponse?, Throwable?> {
        return LiveEndBroadcastRequest(broadcastId, true)
            .execute(igClient)
            .pair()
    }

    fun getLiveInfo(broadcastId: String): Pair<IGResponse?, Throwable?> {
        return LiveBroadcastInfoRequest(broadcastId)
            .execute(igClient)
            .pair()
    }
}