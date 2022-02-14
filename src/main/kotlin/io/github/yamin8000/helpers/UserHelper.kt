package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Utility.actionPair

class UserHelper(private val igClient: IGClient) {

    /**
     * Get user instagram id (pk) based on [username]
     */
    fun getPk(username: String): Dyad<Long?> {
        val (action, error) = igClient.actions.users().findByUsername(username).actionPair()
        return if (action != null && error == null) action.user.pk to null else null to error
    }

    /**
     * Get [User] info based on instagram [userId] (pk)
     */
    private fun getUserInfoByPk(userId: Long): Dyad<User?> {
        return igClient.actions.users().info(userId).actionPair()
    }

    /**
     * Get [User] info based on [username]
     */
    fun getUserInfoByUsername(username: String): Dyad<User?> {
        val (action, error) = igClient.actions.users().findByUsername(username).actionPair()
        return if (action != null && error == null) action.user to null else null to error
    }

    /**
     * Get current logged in [User] info
     */
    fun getCurrentUserInfo(): Dyad<User?> {
        val (action, error) = igClient.actions.account().currentUser().actionPair()
        return if (action != null && error == null) action.user to null else null to error
    }

    fun getCurrentUserPk() : Dyad<Long?> {
        val (action, error) = igClient.actions.account().currentUser().actionPair()
        return if (action != null && error == null) action.user.pk to null else null to error
    }
}