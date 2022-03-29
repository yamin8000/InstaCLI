package io.github.yamin8000.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import io.github.yamin8000.Dyad
import io.github.yamin8000.console.ConsoleHelper.readCleanLine
import java.util.concurrent.Callable

class LoginHelper() {

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
    fun logInWithChallenge(username: String, password: String): Dyad<IGClient?> {
        val challengeHandler = getChallengeHandler()

        return try {
            client.username(username).password(password).onChallenge(challengeHandler).login() to null
        } catch (e: Exception) {
            null to e
        }
    }

    private fun getChallengeHandler(): IGClient.Builder.LoginHandler {
        val inputCode = Callable {
            print("Please input code: ")
            readCleanLine()
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
        return challengeHandler
    }
}