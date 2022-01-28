package io.github.yamin8000.utils

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal

object Constants {

    const val IS_DEBUG_MODE = true

    val ter = Terminal()
    val errorStyle = TextColors.red + TextStyles.bold
    val askStyle = TextColors.cyan + TextStyles.bold
    val menuTextStyle = TextColors.blue + TextStyles.bold

    var downloadDir = "D:/instaKiller"
    const val OK = "ok"
    var MAX_COUNT = Integer.MAX_VALUE
    const val sleepDelay = 100L

    const val LOADING = "Loading..."
    val animations = listOf(
        "▁▂▃▄▅▆▇█▇▆▅▄▃▂▁",
        "←↖↑↗→↘↓↙",
        "▖▘▝▗",
        "┤┘┴└├┌┬┐",
        "◢◣◤◥",
        "◰◳◲◱",
        "◴◷◶◵",
        "◐◓◑◒",
        "◡◡⊙⊙◠◠",
        "⣾⣽⣻⢿⡿⣟⣯⣷",
        "⠁⠂⠄⡀⢀⠠⠐⠈",
        "⠋⠙⠹⠸⠼⠴⠦⠧⠇",
        "⠏⠟⠻⠿⢿⣿⣿⣿⣿",
    )
    var currentLoadingAnimation = 0

    val affirmatives = listOf(
        "y",
        "yes",
        "true",
        "1",
        "yep",
        "yeah",
        "yup",
        "yuh",
        "بله",
        "آره",
        "باشه",
        "نعم",
        "да",
        "давай",
        "давайте",
        "si",
        "oui",
        "ja",
        "ok",
        "okay"
    )
}