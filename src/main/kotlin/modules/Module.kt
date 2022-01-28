package yamin.modules

import yamin.console.printlnC
import yamin.console.ConsoleHelper.getIntegerInput
import yamin.console.ConsoleHelper.pressEnterToContinue
import yamin.console.printC
import yamin.helpers.LoggerHelper.printBlackBar
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
            printlnC { (line + line.extraSpaces(maxLineLength)).blue.bold.reverse }
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