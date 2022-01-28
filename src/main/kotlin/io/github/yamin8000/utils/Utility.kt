package io.github.yamin8000.utils

import com.github.instagram4j.instagram4j.responses.IGResponse
import io.github.yamin8000.utils.Constants.OK
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

    fun <T : IGResponse> CompletableFuture<T>.pair(): Pair<T?, Throwable?> {
        return try {
            val response = this.get()
            if (response.status == OK) response to null
            else response to IllegalStateException("Response status is not OK: ${response.status}")
        } catch (e: Throwable) {
            null to e
        }
    }

    fun requirePositiveLimit(limit: Int) {
        if (limit < 1) throw IllegalArgumentException("Limit must be greater than 0")
    }
}