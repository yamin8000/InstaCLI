package yamin.utils

import utils.printlnC
import yamin.utils.Constants.affirmatives
import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(message: String? = null, range: IntRange? = null): Int {
        if (message != null) printlnC { message.bold.cyan }
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
        printlnC { error.bold.red }
        return getIntegerInput(message, range)
    }

    private fun checkIfInputIsInRange(number: Int, range: IntRange?): Boolean {
        return if (range == null) true else number in range
    }

    fun Scanner.getBooleanInput(): Boolean {
        return try {
            this.nextLine().trim().lowercase(Locale.getDefault()) in affirmatives
        } catch (exception: Exception) {
            false
        }
    }

    fun Scanner.pressEnterToContinue() {
        printlnC { "Press enter to continue...".bold.reverse }
        this.nextLine()
    }

    fun Scanner.getMultipleStrings(field: String): List<String> {
        printlnC {
            """
            Please enter $field/${field}s:
            If there are more than one $field separate them using a comma (,)
            Example: ${field}1,${field}2,${field}3
        """.trimIndent().blue.bold
        }
        val input = this.nextLine().trim().split(",").map { it.trim() }
        return if (input.isValid()) {
            printlnC { "Please enter at least one $field.".red.bold }
            this.getMultipleStrings(field)
        } else input
    }

    private fun List<String>.isValid() = !(this.isEmpty() || this.all { it.isNotEmpty() })
}