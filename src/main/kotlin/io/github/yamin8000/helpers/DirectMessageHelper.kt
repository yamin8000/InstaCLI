package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.direct.Inbox
import com.github.instagram4j.instagram4j.requests.direct.DirectInboxRequest
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Utility.pair

class DirectMessageHelper(private val igClient: IGClient) {

    fun sendDirectMessageByPks(message: String, vararg pkS: Long): Dyad<Boolean?> {
        val (sentResponse, error) = igClient.sendRequest(
            DirectThreadsBroadcastRequest(
                DirectThreadsBroadcastRequest.BroadcastTextPayload(message, *pkS)
            )
        ).pair()
        return if (sentResponse != null && error == null) true to null else null to error
    }

    fun getInbox(): Dyad<Inbox?> {
        val (response, error) = igClient.sendRequest(DirectInboxRequest()).pair()
        return if (response != null && error == null) response.inbox to null else null to error
    }
}