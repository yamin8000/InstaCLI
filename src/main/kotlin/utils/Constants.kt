package yamin.utils

object Constants {

    const val IS_DEBUG_MODE = true

    val loginMenu = """
        0. Show menu
        1. Login with username and password
        2. Login from saved sessions
        3. Exit
    """.trimIndent()

    val mainMenu = """
        0. Show menu
        1. Show user's info
        2. Search for a user
        3. Show user's posts
        4. Send direct message to user/users
        5. Show user's friends (followers/following)
        6. Save users' posts' images
        7. Save users' profile pictures
        9. Settings
        10. Exit
    """.trimIndent()

    val settingSubmenuText = """
        0. Loading animation type
    """.trimIndent()

    const val OK = "ok"
    const val YES = "y"
    const val NO = "n"

    var MAX_COUNT = Integer.MAX_VALUE
    const val LIMIT_COUNT = 10

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