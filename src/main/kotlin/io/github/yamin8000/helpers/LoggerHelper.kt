package io.github.yamin8000.helpers

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import io.github.yamin8000.utils.Constants.IS_DEBUG_MODE
import io.github.yamin8000.utils.Constants.LOADING
import io.github.yamin8000.utils.Constants.animations
import io.github.yamin8000.utils.Constants.currentLoadingAnimation
import io.github.yamin8000.utils.Constants.sleepDelay
import io.github.yamin8000.utils.Constants.ter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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
            val taggedMessage = ">>-- $message --<<"
            if (source != null) ter.println(textStyle("${source.javaClass.enclosingClass.name}:${source.javaClass.enclosingMethod.name}$taggedMessage"))
            else ter.println(textStyle(taggedMessage))
        }
    }

    fun <T> loading(
        cycleWaitTime: Long = sleepDelay,
        function: (() -> Unit) -> T
    ): T {
        val animation = animations[currentLoadingAnimation]

        val executor = Executors.newSingleThreadExecutor()

        var i = 0
        val runnable = Runnable {
            try {
                while (true) {
                    ter.print(TextColors.brightMagenta(animation[i % animation.length].toString()))
                    TimeUnit.MILLISECONDS.sleep(cycleWaitTime)
                    print("\b")
                    i++
                }
            } catch (_: Exception) {

            }
        }

        executor.execute(runnable)

        return function {
            for (j in 0..LOADING.length + 1) print("\b")
            executor.shutdownNow()
        }
    }

    fun <T> progress(
        cycleWaitTime: Long = sleepDelay,
        function: (() -> Unit) -> T
    ): T {
        val animation = animations[currentLoadingAnimation]
        val total = animation.length
        var progression = 0

        val executor = Executors.newSingleThreadExecutor()

        val runnable = Runnable {
            try {
                while (true) {
                    progression++
                    ter.print(TextColors.brightMagenta(animation[progression % total].toString()))
                    TimeUnit.MILLISECONDS.sleep(cycleWaitTime)
                }
            } catch (_: Exception) {

            }
        }

        executor.execute(runnable)

        return function {
            for (i in 0..progression) print("\b")
            executor.shutdownNow()
        }
    }
}