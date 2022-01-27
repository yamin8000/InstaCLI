package yamin.utils

import utils.printlnC
import yamin.utils.Constants.affirmatives
import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(): Int {
        return try {
            val input = this.nextLine().trim()
            if (input.isNotBlank() && input.all { it.isDigit() }) input.toInt() else {
                printlnC { "Please enter a number only, try again!".red.bold }
                getIntegerInput()
            }
        } catch (exception: NumberFormatException) {
            -1
        }
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
        printlnC { "Please enter $field/${field}s:".blue.bold }
        printlnC { "If there are more than one $field separate them using a comma (,)".blue.bold }
        printlnC { "Example: ${field}1,${field}2,${field}3".blue.bold }
        val input = this.nextLine().trim().split(",").map { it.trim() }
        return if (input.isEmpty() || input.all { it.isEmpty() }) {
            printlnC { "Please enter at least one $field.".red.bold }
            this.getMultipleStrings(field)
        } else input
    }
}