package yamin.utils

import utils.printlnC
import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(): Int {
        return try {
            this.nextLine().trim().toInt()
        } catch (exception: NumberFormatException) {
            -1
        }
    }

    fun Scanner.getBooleanInput(): Boolean {
        return try {
            this.nextLine().trim().lowercase(Locale.getDefault()) in arrayOf("y", "yes", "true", "1")
        } catch (exception: Exception) {
            false
        }
    }

    fun Scanner.pressEnterToContinue() {
        printlnC { "Press enter key to continue...".bold.blink }
        this.nextLine()
    }
}