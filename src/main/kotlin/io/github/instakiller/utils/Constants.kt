package io.github.instakiller.utils

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal

object Constants {

    const val IS_DEBUG_MODE = true

    val ter = Terminal()
    val resultStyle = TextColors.green
    val infoStyle = TextColors.brightMagenta + TextStyles.bold
    val errorStyle = TextColors.red + TextStyles.bold
    val askStyle = TextColors.cyan + TextStyles.bold
    val warningStyle = TextColors.yellow + TextStyles.bold
    val menuStyle = TextColors.blue + TextStyles.bold

    const val DOWNLOAD_FOLDER = "DOWNLOAD_FOLDER"
    var downloadDir = "downloads"
    var MAX_COUNT = Integer.MAX_VALUE
    var PAGE_LIMIT = 100
    const val sleepDelay = 100L
    const val animationDelay = 50L

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
        "▉▊▋▌▍▎▏▎▍▌▋▊▉",
    )
    const val LOADING_ANIMATION = "LOADING_ANIMATION"
    var currentLoadingAnimation = 0

    const val SESSION_AUTOSAVE = "SESSION_AUTOSAVE"
    var isAutosavingSession = true

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