package io.github.instakiller.helpers

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import io.github.instakiller.Dyad
import io.github.instakiller.console.ConsoleHelper.readCleanLine
import java.util.concurrent.Callable

class LoginHelper {

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
        val twoFactorHandler = get2faHandler()

        return try {
            client.username(username)
                .password(password)
                .onChallenge(challengeHandler)
                .onTwoFactor(twoFactorHandler)
                .login() to null
        } catch (e: Exception) {
            null to e
        }
    }

    private fun challengeLoginHandler(
        handler: (IGClient, LoginResponse, Callable<String>) -> LoginResponse
    ): IGClient.Builder.LoginHandler {
        val inputCode = Callable {
            print("Please input code: ")
            readCleanLine()
        }

        return IGClient.Builder.LoginHandler { client, response -> handler(client, response, inputCode) }
    }

    private fun getChallengeHandler(): IGClient.Builder.LoginHandler {
        return challengeLoginHandler { igClient, loginResponse, callable ->
            IGChallengeUtils.resolveChallenge(igClient, loginResponse, callable)
        }
    }

    private fun get2faHandler(): IGClient.Builder.LoginHandler {
        return challengeLoginHandler { igClient, loginResponse, callable ->
            IGChallengeUtils.resolveTwoFactor(igClient, loginResponse, callable)
        }
    }
}