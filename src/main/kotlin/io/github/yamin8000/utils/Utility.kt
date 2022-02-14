package io.github.yamin8000.utils

import com.github.instagram4j.instagram4j.responses.IGResponse
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.ter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object Utility {

    fun now(): String = isoTime(LocalDateTime.now())

    fun isoTime(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }

    fun isoTimeOfEpoch(epoch: Long): String {
        return isoTime(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))
    }

    fun <T : IGResponse> CompletableFuture<T>.pair(): Dyad<T?> {
        return try {
            val response = this.get()
            if (response.statusCode in 200 until 300) response to null
            else response to IllegalStateException("Response status is not OK: ${response.status}")
        } catch (e: Throwable) {
            null to e
        }
    }

    fun <U> CompletableFuture<U>.actionPair(): Dyad<U?> {
        return try {
            val response = this.get()
            response to null
        } catch (e: Throwable) {
            null to e
        }
    }

    fun requirePositiveLimit(limit: Int) {
        if (limit < 1) throw IllegalArgumentException("Limit must be greater than 0")
    }

    /**
     * This method will inject non-null value to callback if it is not null,
     * otherwise,
     * If data is null error message is shown to user.
     *
     * @param T data type
     * @param message error message
     * @param callback callback for injecting non-null data
     */
    fun <T> Dyad<T?>.solo(message: String? = "Error", callback: (T) -> Unit) {
        if (this.first != null && this.second == null) callback(this.first!!)
        else ter.println(errorStyle("$message: ${this.second?.message}"))
    }
}