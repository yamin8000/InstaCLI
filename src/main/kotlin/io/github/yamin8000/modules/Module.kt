package io.github.yamin8000.modules

import com.github.ajalt.mordant.rendering.TextColors
import io.github.yamin8000.console.ConsoleHelper.getIntegerInput
import io.github.yamin8000.console.ConsoleHelper.pressEnterToContinue
import io.github.yamin8000.helpers.LoggerHelper.printBlackBar
import io.github.yamin8000.utils.Constants.ter
import java.util.*

open class Module(protected val scanner: Scanner, private val menuText: String) {

    open fun run(): Int {
        val subMenus = showMenu()
        return scanner.getIntegerInput(range = 0 until subMenus)
    }

    fun showMenu(): Int {
        scanner.pressEnterToContinue()
        val lines = menuText.split("\n")
        val maxLineLength = lines.maxOf { it.length }
        lines.forEachIndexed { index, line ->
            if (index == 0) printBlackBar(maxLineLength)
            ter.println(TextColors.magenta((line + line.extraSpaces(maxLineLength))))
            if (index == lines.size - 1) printBlackBar(maxLineLength)
        }
        println()
        return lines.size
    }

    private fun String.extraSpaces(maxLineLength: Int): String {
        val spaces = maxLineLength - this.length
        return buildString { for (i in 0 until spaces) append(" ") }
    }
}