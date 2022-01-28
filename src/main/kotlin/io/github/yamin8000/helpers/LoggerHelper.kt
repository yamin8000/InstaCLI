package io.github.yamin8000.helpers

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import io.github.yamin8000.utils.Constants.IS_DEBUG_MODE
import io.github.yamin8000.utils.Constants.LOADING
import io.github.yamin8000.utils.Constants.MAX_COUNT
import io.github.yamin8000.utils.Constants.animations
import io.github.yamin8000.utils.Constants.currentLoadingAnimation
import io.github.yamin8000.utils.Constants.sleepDelay
import io.github.yamin8000.utils.Constants.ter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LoggerHelper {

    fun loggerD(message: String, source: (() -> Unit)? = null) {
        val style = TextColors.yellow
        logger(style, message, source)
    }

    fun loggerE(message: String, source: (() -> Unit)? = null) {
        val style = TextColors.red
        logger(style, message, source)
    }

    private fun logger(textStyle: TextStyle, message: String, source: (() -> Unit)? = null) {
        if (IS_DEBUG_MODE) {
            val taggedMessage = ">>--$message--<<"
            if (source != null) ter.println(textStyle("${source.javaClass.enclosingClass.name}:${source.javaClass.enclosingMethod.name}$taggedMessage"))
            else ter.println(textStyle(taggedMessage))
        }
    }

    fun printBlackBar(length: Int) {
        //printlnC { buildString { for (i in 0 until length) append(" ") }.black.bright.reverse }
    }

    fun loading(
        cycleWaitTime: Long = sleepDelay,
        cycles: Int = MAX_COUNT,
        function: (() -> Unit) -> Unit
    ) {
        val animation = animations[currentLoadingAnimation]
        val job = CoroutineScope(Dispatchers.Default).launch {
            repeat(cycles) {
                ter.print(TextColors.red(animation[it % animation.length].toString()))
                delay(cycleWaitTime)
                print("\b")
            }
        }
        function {
            repeat(LOADING.length + 1) { print("\b") }
            job.cancel()
        }
    }

    fun progress(
        cycleWaitTime: Long = sleepDelay,
        cycles: Int = MAX_COUNT,
        function: (() -> Unit) -> Unit
    ) {
        val animation = animations[currentLoadingAnimation]
        val total = animation.length
        var progression = 0
        val job = CoroutineScope(Dispatchers.Default).launch {
            repeat(cycles) {
                progression++
                ter.print(TextColors.magenta(animation[it % total].toString()))
                delay(cycleWaitTime)
            }
        }
        function {
            repeat(progression) { print("\b") }
            job.cancel()
        }
    }
}