package yamin.utils

import java.util.*

object ConsoleHelper {

    fun Scanner.getIntegerInput(): Int {
        return try {
            this.nextLine().trim().toInt()
        } catch (exception: NumberFormatException) {
            -1
        }
    }
}