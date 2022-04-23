package io.github.instakiller.console

import com.github.ajalt.mordant.rendering.TextColors
import io.github.instakiller.helpers.LoggerHelper.randHsv
import io.github.instakiller.utils.Constants.affirmatives
import io.github.instakiller.utils.Constants.askStyle
import io.github.instakiller.utils.Constants.errorStyle
import io.github.instakiller.utils.Constants.infoStyle
import io.github.instakiller.utils.Constants.ter
import java.util.*

/**
 * Helper class for console input/output
 */
object ConsoleHelper {

    private const val integerInputFailure = "Please enter a number only, try again!"

    /**
     * Prompts the user for an [Integer] input with the given optional [message],
     * the number must be between [range] if included otherwise any [Integer] is acceptable.
     * Eventually return the input as [Int]
     */
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

    /**
     * Extension function for checking if [Int] is in the given [range]
     */
    private fun Int.isInRange(range: IntRange?) = range == null || this in range

    /**
     * If the input is not valid (refer to [readInteger]), prompt the user again for an [Integer] input
     */
    private fun readIntegerAfterFailure(error: String, message: String?, range: IntRange?): Int {
        ter.println(errorStyle(error))
        return readInteger(message, range)
    }

    /**
     * Prompts the user for a [Boolean] input with the given optional [message],
     * and eventually return the input as [Boolean]
     */
    fun readBoolean(message: String? = null): Boolean {
        return try {
            if (message != null) ter.println(askStyle(message))
            readCleanLine().lowercase(Locale.getDefault()) in affirmatives
        } catch (exception: Exception) {
            false
        }
    }

    /**
     * Prompts the user to press enter to continue
     */
    fun pressEnterToContinue(message: String = "continue...") {
        ter.println((TextColors.yellow on TextColors.black)("Press enter to $message"))
        readCleanLine()
    }

    /**
     * Prompts the user for entering multiple [String] values,
     * and eventually return a [List] of [String]
     */
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

    /**
     * Prompts the user for a single [String] value,
     * and eventually return the input as [String]
     */
    fun readSingleString(field: String): String {
        ter.println(askStyle("Please enter ") + infoStyle(field))
        return readCleanLine().ifBlank {
            ter.println(errorStyle("Input cannot be empty, try again!"))
            this.readSingleString(field)
        }
    }

    /**
     * Reads a line from input and [String.trim] it,
     * if the input is null then returns an empty [String]
     */
    fun readCleanLine(): String = readlnOrNull() ?: "".trim()

    private fun List<String>.isValid() = !(this.isEmpty() || this.all { it.isNotEmpty() })
}