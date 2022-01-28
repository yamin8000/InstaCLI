package io.github.yamin8000.utils

object Menus {

    val initMenu = """
        0. Show menu
        1. Login with username and password
        2. Login from saved sessions
        3. Exit
    """.trimIndent()

    val settingSubmenuText = """
        0. Back
        1. Exit
        2. Change loading animation type
        3. Change download folder
    """.trimIndent()

    val mainMenu = """
        0. Back(exit)
        1. Show menu
        2. User info
        3. Post info
        4. Friend info (follower/following/etc)
        5. Search
        6. Direct Message
        7. Settings
    """.trimIndent()


    val userMenu = """
            0. Back
            1. Show menu
            2. Show current user info
            3. Show user info by username
            4. Save user profile picture
    """.trimIndent()

    val postMenu = """
            0. Back
            1. Show menu
            2. Show user/users posts by username
            3. Save user/users posts images
    """.trimIndent()

    val directMessageMenu = """
        0. Back
        1. Show menu
        2. Show direct messages (inbox)
        3. Send direct message
    """.trimIndent()

    val friendsMenu = """
        0. Back
        1. Show menu
        2. Show current user followers
        3. Show current user following
        4. Show user followers
        5. Show user following
    """.trimIndent()
}