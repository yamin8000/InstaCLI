package yamin.utils

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import java.util.*
import java.util.concurrent.Callable

class LoginHelper(private val username: String, private val password: String) {

    private val client = IGClient.builder()

    fun logIn(): IGClient {
        return client.username(username).password(password).login()
    }

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