package yamin.utils

import com.github.instagram4j.instagram4j.responses.IGResponse
import yamin.utils.Constants.OK
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object Utility {

    fun now(): String = isoTime(LocalDateTime.now())

    private fun isoTime(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }

    fun <T : IGResponse> CompletableFuture<T>.pair(): Pair<T?, Throwable?> {
        return try {
            val response = this.get()
            if (response.status == OK) response to null
            else response to IllegalStateException("Response status is not OK: ${response.status}")
        } catch (e: Throwable) {
            null to e
        }
    }

    fun limitMustNotBeNegative(limit: Int) {
        if (limit < 1) throw IllegalArgumentException("Limit must be greater than 0")
    }
}