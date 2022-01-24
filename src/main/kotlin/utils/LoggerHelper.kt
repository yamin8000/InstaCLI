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

    suspend fun loadingAsync(cycleWaitTime: Long = 1000, cycles: Int = 60) {
        repeat(cycles) {
            when (it % 5) {
                0 -> printC { "▮".white }
                1 -> printC { "▮".yellow.bright }
                2 -> printC { "▮".yellow }
                3 -> printC { "▮".red.bright }
                4 -> printC { "▮".red }
                else -> printC { "▮".red.bright.blink }
            }
            delay(cycleWaitTime)
        }
    }
}