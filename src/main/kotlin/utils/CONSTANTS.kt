package yamin.utils

object CONSTANTS {
    const val OK = "ok"
    const val YES = "y"
    const val NO = "n"
    var POST_LIMIT_COUNT = Integer.MAX_VALUE
    const val IS_DEBUG_MODE = true
    val menuText = """
        0. Show Menu
        1. Login with username password
        2. Login from saved sessions
        3. User posts
        4. Send direct message
        5. Get friends (followers/following)
        6. Download users' posts' images
    """.trimIndent()
}