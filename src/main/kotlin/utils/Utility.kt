package yamin.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utility {

    fun now(): String = isoTime(LocalDateTime.now())

    private fun isoTime(localDateTime: LocalDateTime): String {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime)
    }
}