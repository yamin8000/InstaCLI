package yamin.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import java.util.*
import java.util.concurrent.Callable

object LoginHelper {

    private val client = IGClient.builder()

    /**
     * Log in with given [username] and [password]
     * @param username Username
     * @param password Password
     * @throws com.github.instagram4j.instagram4j.exceptions.IGLoginException
     */
    fun logIn(username: String, password: String): IGClient {
        return client.username(username).password(password).login()
    }

    /**
     * Log in with given [username] and [password]
     *
     * and also handles log in challenge if it happens.
     * @param username Username
     * @param password Password
     * @throws com.github.instagram4j.instagram4j.exceptions.IGLoginException
     */
    fun logInWithChallenge(username: String, password: String): IGClient {
        val scanner = Scanner(System.`in`)

        val inputCode = Callable {
            print("Please input code: ")
            scanner.nextLine()
        }
        val challengeHandler = IGClient.Builder.LoginHandler { client, response ->
            if (client != null && response != null) {
                IGChallengeUtils.resolveChallenge(
                    client,
                    response,
                    inputCode
                )
            } else null
        }
        return client.username(username).password(password).onChallenge(challengeHandler).login()
    }
}