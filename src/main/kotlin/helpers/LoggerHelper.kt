package yamin.helpers

import kotlinx.coroutines.*
import utils.printC
import utils.printlnC
import yamin.utils.Constants.IS_DEBUG_MODE
import yamin.utils.Constants.LOADING
import yamin.utils.Constants.MAX_COUNT
import yamin.utils.Constants.animations
import yamin.utils.Constants.currentLoadingAnimation
import yamin.utils.Constants.sleepDelay

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

    fun loading(
        cycleWaitTime: Long = sleepDelay,
        cycles: Int = MAX_COUNT,
        function: (() -> Unit) -> Unit
    ) {
        val animation = animations[currentLoadingAnimation]
        val job = CoroutineScope(Dispatchers.Default).launch {
            repeat(cycles) {
                printC { animation[it % animation.length].bold.red.bright }
                delay(cycleWaitTime)
                print("\b")
            }
        }
        function {
            repeat(LOADING.length + 1) { print("\b") }
            job.cancel()
        }
    }
}