package io.github.yamin8000.utils

import com.github.instagram4j.instagram4j.responses.IGResponse
import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.ter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

/**
 * Utility class containing various utility functions
 */
object Utility {

    /**
     * returns the current time in ISO 8601 format
     */
    fun now(): String = isoTime(LocalDateTime.now())

    /**
     * returns [String] form of [localDateTime] in ISO 8601 format
     */
    fun isoTime(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }

    /**
     * returns [String] form of [epoch] in ISO 8601 format with [ZoneOffset.UTC]
     */
    fun isoTimeOfEpoch(epoch: Long): String {
        return isoTime(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))
    }

    /**
     * returns [Dyad] instance containing [T]/[IGResponse] and [Exception] related to request for [T]
     */
    fun <T : IGResponse> CompletableFuture<T>.pair(): Dyad<T?> {
        return try {
            val response = this.get()
            if (response.statusCode in 200 until 300) response to null
            else response to IllegalStateException("Response status is not OK: ${response.status}")
        } catch (e: Throwable) {
            null to e
        }
    }

    /**
     * returns [Dyad] instance containing [U]/[IGResponse] and [Exception] related to action for [U]
     */
    fun <U> CompletableFuture<U>.actionPair(): Dyad<U?> {
        return try {
            val response = this.get()
            response to null
        } catch (e: Throwable) {
            null to e
        }
    }

    /**
     * @throws [IllegalArgumentException] if [limit] is lesser than 1
     */
    fun requirePositiveLimit(limit: Int) {
        if (limit < 1) throw IllegalArgumentException("Limit must be greater than 0")
    }

    /**
     * This method will notify the callback with a non-null value if it is not null,
     * otherwise,
     * If data is null error message is shown to user.
     *
     * @param T data type
     * @param message error message
     * @param success callback for injecting non-null data
     * @param failed callback for showing error message
     */
    fun <T> Dyad<T?>.solo(
        success: (T) -> Unit,
        failed: ((Throwable?) -> Unit)? = null,
        message: String? = "Error"
    ) {
        if (this.first != null && this.second == null) success(this.first!!)
        else {
            ter.println(errorStyle("$message: ${this.second?.message}"))
            failed?.invoke(this.second)
        }
    }

    /**
     * return name of the given url
     */
    fun String.getName() = this.substringAfterLast("/").substringBefore("?")
}