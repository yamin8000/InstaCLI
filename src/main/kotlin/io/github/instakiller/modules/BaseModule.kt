package io.github.instakiller.modules

import com.github.ajalt.mordant.rendering.BorderType
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.table
import io.github.instakiller.console.ConsoleHelper.readInteger
import io.github.instakiller.utils.Constants.ter

open class BaseModule(private val menuText: String) {

    private val style = TextColors.blue + TextStyles.bold

    open fun run(): Int {
        val subMenus = showMenu()
        return readInteger(range = 0 until subMenus)
    }

    fun showMenu(): Int {
        val lines = menuText.split("\n")
        ter.println(table {
            borderType = BorderType.ROUNDED
            borderStyle = TextColors.brightBlue
            body { lines.forEach { row(style(it)) } }
        })
        return lines.size
    }
}