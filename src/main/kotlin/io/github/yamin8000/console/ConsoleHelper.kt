package io.github.yamin8000.console

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import io.github.yamin8000.utils.Constants.affirmatives
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.ter
import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(message: String? = null, range: IntRange? = null): Int {
        if (message != null) ter.println(TextColors.blue(message))
        return try {
            val input = this.nextLine().trim()
            if (input.isNotBlank() && input.all { it.isDigit() }) {
                val number = input.toInt()
                if (checkIfInputIsInRange(number, range)) number
                else getIntegerInputFailure("Input is out of range.", message, range)
            } else getIntegerInputFailure("Please enter a number only, try again!", message, range)
        } catch (exception: NumberFormatException) {
            -1
        }
    }

    private fun Scanner.getIntegerInputFailure(error: String, message: String?, range: IntRange?): Int {
        ter.println(errorStyle(error))
        return getIntegerInput(message, range)
    }

    private fun checkIfInputIsInRange(number: Int, range: IntRange?): Boolean {
        return if (range == null) true else number in range
    }

    fun Scanner.getBooleanInput(message: String? = null): Boolean {
        return try {
            if (message != null) ter.println(TextColors.blue(message))
            this.nextLine().trim().lowercase(Locale.getDefault()) in affirmatives
        } catch (exception: Exception) {
            false
        }
    }

    fun Scanner.pressEnterToContinue() {
        ter.println(TextColors.white.bg("Press enter to continue..."))
        this.nextLine()
    }

    fun Scanner.getMultipleStrings(field: String): List<String> {
        ter.println(
            TextStyles.bold(
                """
            Please enter $field/${field}s:
            If there are more than one $field separate them using a comma (,)
            Example: ${field}1,${field}2,${field}3
        """.trimIndent()
            )
        )
        val input = this.nextLine().trim().split(",").map { it.trim() }
        return if (input.isValid()) {
            ter.println(errorStyle("Please enter at least one $field."))
            this.getMultipleStrings(field)
        } else input
    }

    private fun List<String>.isValid() = !(this.isEmpty() || this.all { it.isNotEmpty() })
}