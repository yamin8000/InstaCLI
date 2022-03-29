package io.github.yamin8000.console

import com.github.ajalt.mordant.rendering.TextColors
import io.github.yamin8000.helpers.LoggerHelper.randHsv
import io.github.yamin8000.utils.Constants.affirmatives
import io.github.yamin8000.utils.Constants.askStyle
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.infoStyle
import io.github.yamin8000.utils.Constants.ter
import java.util.*

object ConsoleHelper {

    private val integerInputFailure = "Please enter a number only, try again!"

    fun readInteger(message: String? = null, range: IntRange? = null): Int {
        if (message != null) ter.println(askStyle(message))
        return try {
            val input = readCleanLine()
            if (input.isNotBlank() && input.all { it.isDigit() }) {
                val number = input.toInt()
                if (number.isInRange(range)) number
                else readIntegerAfterFailure("Input is out of range.", message, range)
            } else readIntegerAfterFailure(integerInputFailure, message, range)
        } catch (exception: NumberFormatException) {
            readIntegerAfterFailure(integerInputFailure, message, range)
        }
    }

    private fun Int.isInRange(range: IntRange?) = range == null || this in range

    private fun readIntegerAfterFailure(error: String, message: String?, range: IntRange?): Int {
        ter.println(errorStyle(error))
        return readInteger(message, range)
    }

    fun readBoolean(message: String? = null): Boolean {
        return try {
            if (message != null) ter.println(askStyle(message))
            readCleanLine().lowercase(Locale.getDefault()) in affirmatives
        } catch (exception: Exception) {
            false
        }
    }

    fun pressEnterToContinue(message: String = "continue...") {
        ter.println((TextColors.yellow on TextColors.black)("Press enter to $message"))
        readCleanLine()
    }

    fun readMultipleStrings(field: String): List<String> {
        ter.println(askStyle("Please enter ${infoStyle("$field/${field}s")}"))
        ter.println(askStyle("If there are more than one ${infoStyle(field)} separate them using a comma (${infoStyle(",")})"))
        ter.print(askStyle("Example: "))
        ter.println("${randHsv()("John")},${randHsv()("Paul")},${randHsv()("George")},${randHsv()("Ringo")}")
        val input = readCleanLine().split(",").map { it.trim() }
        return if (input.isValid()) {
            ter.println(errorStyle("Please enter at least one $field."))
            readMultipleStrings(field)
        } else input
    }

    fun readSingleString(field: String): String {
        ter.println(askStyle("Please enter ") + infoStyle(field))
        return readCleanLine().ifBlank {
            ter.println(errorStyle("Input cannot be empty, try again!"))
            this.readSingleString(field)
        }
    }

    fun readCleanLine(): String = readlnOrNull() ?: "".trim()

    private fun List<String>.isValid() = !(this.isEmpty() || this.all { it.isNotEmpty() })
}