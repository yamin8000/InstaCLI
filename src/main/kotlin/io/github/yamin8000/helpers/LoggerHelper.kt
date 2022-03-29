package io.github.yamin8000.helpers

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import io.github.yamin8000.utils.Constants.IS_DEBUG_MODE
import io.github.yamin8000.utils.Constants.LOADING
import io.github.yamin8000.utils.Constants.animationDelay
import io.github.yamin8000.utils.Constants.animations
import io.github.yamin8000.utils.Constants.currentLoadingAnimation
import io.github.yamin8000.utils.Constants.ter
import java.util.concurrent.ExecutorService
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
        cycleWaitTime: Long = animationDelay,
        function: (() -> Unit) -> T
    ): T {
        val animation = getAnimationString()

        val executor = Executors.newSingleThreadExecutor()

        var colorIndex = (0..360).random()
        val runnable = Runnable {
            try {
                while (true) {
                    ter.print(getHsvColorStyle(colorIndex)(animation[colorIndex % animation.length].toString()))
                    TimeUnit.MILLISECONDS.sleep(cycleWaitTime)
                    print("\b")
                    colorIndex++
                }
            } catch (_: Exception) {
                shutdownAndClear(executor, LOADING.length + 1)
            }
        }

        executor.execute(runnable)

        return function {
            shutdownAndClear(executor, LOADING.length + 1)
        }
    }

    fun <T> progress(
        cycleWaitTime: Long = animationDelay,
        function: (() -> Unit) -> T
    ): T {
        val animation = getAnimationString()
        val total = animation.length
        var progression = 0

        val executor = Executors.newSingleThreadExecutor()

        var colorIndex = (0..360).random()
        val runnable = Runnable {
            try {
                while (true) {
                    progression++
                    ter.print(getHsvColorStyle(colorIndex)(animation[progression % total].toString()))
                    TimeUnit.MILLISECONDS.sleep(cycleWaitTime)
                    colorIndex++
                }
            } catch (_: Exception) {
                shutdownAndClear(executor, progression)
            }
        }

        executor.execute(runnable)

        return function { shutdownAndClear(executor, progression) }
    }

    private fun shutdownAndClear(executorService: ExecutorService, animationLength: Int) {
        for (i in 0..animationLength) print("\b")
        executorService.shutdownNow()
    }

    private fun getAnimationString(): String {
        return if (currentLoadingAnimation in 0..animations.size)
            animations[currentLoadingAnimation]
        else animations[0]
    }

    private fun getHsvColorStyle(colorIndex: Int) = ter.colors.hsv((colorIndex * 5) % 360, 1, 1)

    fun randHsv() = ter.colors.hsv((0..360).random(), 1, 1)
}