package io.github.instakiller.utils

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
        4. Change session auto save behaviour
    """.trimIndent()

    val mainMenu = """
        0. Back(exit)
        1. Show menu
        2. User info
        3. Post info
        4. Friend info (follower/following/etc)
        5. Search
        6. Direct Message
        7. Live stream
        8. Account
        9. Story
        10. Settings
    """.trimIndent()


    val userMenu = """
            0. Back
            1. Show menu
            2. Show user info by username
            3. Save user profile picture
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
        3. Send direct message to user/users
    """.trimIndent()

    val friendsMenu = """
        0. Back
        1. Show menu
        2. Show current user followers
        3. Show current user following
        4. Show current user unfollowers
        5. Show user followers
        6. Show user following
        7. Show user unfollowers
    """.trimIndent()

    val livestreamMenu = """
        0. Back
        1. Show menu
        2. Create livestream
        3. Start livestream
        4. Stop livestream
        5. Show raw livestreams info
        6. Show livestream viewer count
        7. Show livestream viewer list
        8. Add comment to livestream
        9. Show livestream comments list
    """.trimIndent()

    val accountMenu = """
        0. Back
        1. Show menu
        2. Show current user info
        3. Change account bio
    """.trimIndent()

    val storyMenu = """
        0. Back
        1. Show menu
        2. Show current user stories
        3. Save users stories
    """.trimIndent()
}