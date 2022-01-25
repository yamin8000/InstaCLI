package yamin.helpers

import kotlinx.coroutines.*
import utils.printC
import utils.printlnC
import yamin.utils.CONSTANTS.IS_DEBUG_MODE
import yamin.utils.CONSTANTS.sleepDelay

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

    fun loadingAsync(cycleWaitTime: Long = sleepDelay, cycles: Int = 60) = CoroutineScope(Dispatchers.Default).launch {
        val animation = "▁▂▃▄▅▆▇█▇▆▅▄▃▂▁"
        repeat(cycles) {
            printC { animation[it % animation.length].bold.red.bright }
            delay(cycleWaitTime)
            print("\b")
        }
    }
}