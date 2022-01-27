package yamin.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest
import com.github.instagram4j.instagram4j.requests.users.UsersInfoRequest
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest
import yamin.Dyad
import yamin.utils.Utility.pair

class UserHelper(private val igClient: IGClient) {


    /**
     * Get user instagram id (pk) based on [username]
     */
    fun getPk(username: String): Dyad<Long?> {
        val (response, error) = igClient.sendRequest(UsersUsernameInfoRequest(username)).pair()
        return if (response != null && error == null) response.user.pk to null else null to error
    }

    /**
     * Get [User] info based on instagram [userId] (pk)
     */
    private fun getUserInfoByPk(userId: Long): Dyad<User?> {
        val (response, error) = igClient.sendRequest(UsersInfoRequest(userId)).pair()
        return if (response != null && error == null) response.user to null else null to error
    }

    /**
     * Get [User] info based on [username]
     */
    fun getUserInfoByUsername(username: String): Dyad<User?> {
        val (pk, error) = getPk(username)
        return if (pk != null && error == null) {
            val (user, userError) = getUserInfoByPk(pk)
            if (user != null && userError == null) user to null
            else null to userError
        } else null to error
    }

    /**
     * Get current logged in [User] info
     */
    fun getCurrentUserInfo(): Dyad<User?> {
        val (response, error) = igClient.sendRequest(AccountsCurrentUserRequest()).pair()
        return if (response != null && error == null) response.user to null else null to error
    }
}