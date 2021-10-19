package yamin.utils

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import java.util.*
import java.util.concurrent.Callable

class LoginHelper(private val username: String, private val password: String) {

    private val client = IGClient.builder()

    /**
     * log in with given username and password
     *
     * @throws com.github.instagram4j.instagram4j.exceptions.IGLoginException
     *
     */
    fun logIn(): IGClient {
        return client.username(username).password(password).login()
    }

    /**
     * log in with given username and password
     *
     * and also handles log in challenge if it happens
     *
     * @throws com.github.instagram4j.instagram4j.exceptions.IGLoginException
     *
     */
    fun logInWithChallenge(): IGClient {
        val scanner = Scanner(System.`in`)

        val inputCode = Callable {
            print("Please input code: ")
            scanner.nextLine()
        }
        val challengeHandler = IGClient.Builder.LoginHandler { client: IGClient?, response: LoginResponse? ->
            IGChallengeUtils.resolveChallenge(
                client!!,
                response!!,
                inputCode
            )
        }
        return client.username(username).password(password).onChallenge(challengeHandler).login()
    }
}