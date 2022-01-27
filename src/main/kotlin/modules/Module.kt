package yamin.modules

import utils.printlnC
import yamin.utils.ConsoleHelper.getIntegerInput
import yamin.utils.ConsoleHelper.pressEnterToContinue
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
        lines.forEach { printlnC { (it + it.extraSpaces(maxLineLength)).blue.bold.reverse } }
        println()
        return lines.size
    }

    private fun String.extraSpaces(maxLineLength: Int): String {
        val spaces = maxLineLength - this.length
        return buildString { for (i in 0 until spaces) append(" ") }
    }
}