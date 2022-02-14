package io.github.yamin8000.console

import com.github.ajalt.mordant.rendering.TextColors
import io.github.yamin8000.utils.Constants.affirmatives
import io.github.yamin8000.utils.Constants.askStyle
import io.github.yamin8000.utils.Constants.errorStyle
import io.github.yamin8000.utils.Constants.infoStyle
import io.github.yamin8000.utils.Constants.menuStyle
import io.github.yamin8000.utils.Constants.ter
import io.github.yamin8000.utils.Constants.warningStyle
import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(message: String? = null, range: IntRange? = null): Int {
        if (message != null) ter.println(askStyle(message))
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

    private fun checkIfInputIsInRange(number: Int, range: IntRange?): Boolean {
        return if (range == null) true else number in range
    }

    private fun Scanner.getIntegerInputFailure(error: String, message: String?, range: IntRange?): Int {
        ter.println(errorStyle(error))
        return getIntegerInput(message, range)
    }

    fun Scanner.getBooleanInput(message: String? = null): Boolean {
        return try {
            if (message != null) ter.println(askStyle(message))
            this.nextLine().trim().lowercase(Locale.getDefault()) in affirmatives
        } catch (exception: Exception) {
            false
        }
    }

    fun Scanner.pressEnterToContinue(message: String = "continue...") {
        ter.println((TextColors.yellow on TextColors.black)("Press enter to $message"))
        this.nextLine()
    }

    fun Scanner.getMultipleStrings(field: String): List<String> {
        ter.println(
            askStyle(
                """
            Please enter ${infoStyle("$field/${field}s")}
            If there are more than one ${infoStyle(field)} separate them using a comma (${infoStyle(",")})
            Example: ${askStyle("${infoStyle("John")},${warningStyle("Paul")},${errorStyle("George")},${menuStyle("Ringo")}")}
        """.trimIndent()
            )
        )
        val input = this.nextLine().trim().sanitize().split(",").map { it.trim() }
        return if (input.isValid()) {
            ter.println(errorStyle("Please enter at least one $field."))
            this.getMultipleStrings(field)
        } else input
    }

    fun Scanner.getSingleString(field: String): String {
        ter.println(askStyle("Please enter ") + infoStyle(field))
        val input = this.nextLine().trim().sanitize()
        return input.ifBlank {
            ter.println(errorStyle("Input cannot be empty, try again!"))
            this.getSingleString(field)
        }
    }

    private fun List<String>.isValid() = !(this.isEmpty() || this.all { it.isNotEmpty() })

    private fun String.sanitize() = this.replace("\\s".toRegex(), "")
}