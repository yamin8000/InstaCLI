package io.github.yamin8000.utils

import com.fasterxml.jackson.databind.ObjectMapper

object JsonUtils {

    fun Any.pretty(): String {
        val mapper = ObjectMapper()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
    }
}