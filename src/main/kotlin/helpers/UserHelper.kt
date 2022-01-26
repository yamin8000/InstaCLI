package yamin.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.user.User
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest
import com.github.instagram4j.instagram4j.requests.users.UsersInfoRequest
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest
import yamin.utils.Constants
import yamin.utils.Constants.OK
import yamin.utils.Utility.pair

class UserHelper(private val igClient: IGClient) {

    /**
     * Get pk -> primary key -> id of user based on instagram username
     */
    fun getPk(username: String): Pair<Long?, Throwable?> {
        val (response, error) = igClient.sendRequest(UsersUsernameInfoRequest(username)).pair()
        return if (response != null && error == null) response.user.pk to null else null to error
    }

    private fun getUserInfoByPk(userId: Long): Pair<User?, Throwable?> {
        val (response, error) = igClient.sendRequest(UsersInfoRequest(userId)).pair()
        return if (response != null && error == null) response.user to null else null to error
    }

    fun getUserInfoByUsername(username: String): Pair<User?, Throwable?> {
        val (pk, error) = getPk(username)
        return if (pk != null && error == null) {
            val (user, userError) = getUserInfoByPk(pk)
            if (user != null && userError == null) user to null
            else null to userError
        } else null to error
    }

    fun getCurrentUserInfo(): Pair<User?, Throwable?> {
        val (response, error) = igClient.sendRequest(AccountsCurrentUserRequest()).pair()
        return if (response != null && error == null) response.user to null else null to error
    }
}