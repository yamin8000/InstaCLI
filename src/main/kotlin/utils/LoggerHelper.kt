package yamin.utils

import kotlinx.coroutines.delay
import utils.printC
import utils.printlnC
import yamin.utils.CONSTANTS.IS_DEBUG_MODE

object LoggerHelper {

    fun loggerD(message: String, source: (() -> Unit)? = null) {
        if (IS_DEBUG_MODE) {
            val taggedMessage = ">>--$message--<<"
            if (source != null) printlnC { "${source.javaClass.enclosingClass.name}:${source.javaClass.enclosingMethod.name}$taggedMessage".yellow }
            else printlnC { taggedMessage.yellow }
        }
    }

    fun loggerE(message: String, source: (() -> Unit)? = null) {
        if (IS_DEBUG_MODE) {
            val taggedMessage = ">>--$message--<<"
            if (source != null) printlnC { "${source.javaClass.enclosingClass.name}:${source.javaClass.enclosingMethod.name}$taggedMessage".red }
            else printlnC { taggedMessage.red }
        }
    }

    suspend fun loadingAsync(cycleWaitTime: Long = 500, cycles: Int = 60) {
        repeat(cycles) {
            when (it % 4) {
                0 -> printC { "/".bold.yellow.bright }
                1 -> printC { "-".bold.blue.bright }
                2 -> printC { "\\".bold.red.bright }
                3 -> printC { "|".bold.green.bright }
            }
            delay(cycleWaitTime)
            print("\b")
        }
    }
}