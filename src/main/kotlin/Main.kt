package yamin

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.requests.accounts.AccountsCurrentUserRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yamin.utils.JsonUtils.pretty
import yamin.utils.LoginHelper
import yamin.utils.RequestHelper
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture

fun main() {

    menuHandler()

//    try {
//        client.actions().timeline().uploadPhoto(File("clean.jpg"), "asdasda")
//            .thenAccept { println("hello") }.join()
//    } catch (exception: Exception) {
//        println(exception.stackTraceToString())
//    }


    //val data = client.sendRequest(FeedUserRequest(45250133777)).join()
    //val firstId = data.items.first().id
    //val likers = client.sendRequest(MediaGetLikersRequest(firstId)).join()
    //println(likers.users.pretty())
    //val commenters = client.sendRequest(MediaGetCommentsRequest(firstId)).join()
    //println(commenters.comments.pretty())
    //val count = client.sendRequest(UsersInfoRequest(7995957172)).join().user.follower_count
    //println("count $count")
//    val myFollowers = mutableListOf<Profile>()
//    var nextMaxId: String? = ""
//    while (nextMaxId != null) {
//        val friendshipsFeedsRequest =
//            FriendshipsFeedsRequest(7995957172, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, nextMaxId)
//        val feedUsersResponse = client.sendRequest(friendshipsFeedsRequest).join()
//        nextMaxId = feedUsersResponse.next_max_id
//        //println(followers.users.first())
//        myFollowers.addAll(feedUsersResponse.users)
//        Thread.sleep(1000)
//    }
//    println(myFollowers.pretty())
    //val usernames = myFollowers.map { it.username }
    //val uniqueUsernames = mutableListOf<String>()
    //usernames.forEach { if (!uniqueUsernames.contains(it)) uniqueUsernames.add(it) }
    //println(uniqueUsernames.size)
    //println(usernames)

    //DirectThreadsRequest(firstThread.thread_id)
    //println(client.isLoggedIn)

//    var usersSearchResponse = client.sendRequest(UsersSearchRequest("therock")).join()
//    usersSearchResponse.users.forEach { user ->
//        println(user.pk)
//    }

//    var data = client.sendRequest(
//        DirectThreadsBroadcastRequest(
//            DirectThreadsBroadcastRequest.BroadcastTextPayload(
//                "hello there",
//                7995957172
//            )
//        )
//    ).join()
//    println(data)
}

fun menuHandler() {
    val menuText = """
        1. Login with username password
        2. Logic from saved sessions
        3. User Posts
        4. 
        5. 
    """.trimIndent()

    var client: IGClient? = null
    val scanner = Scanner(System.`in`)

    do {
        println(menuText)
        when (getIntegerInput(scanner)) {
            1 -> {
                client = getClientByUsernamePassword(scanner)
                continue
            }
            2 -> {
                client = getClientBySession(scanner)
                if (client == null) println("There's no saved sessions")
                continue
            }
            3 -> {
                if (client != null) handleUserPostsFetcher(scanner, client)
                else println("Please login first!")
                continue
            }
            else -> {
                println("Bad input!")
                continue
            }
        }
    } while (true)
}

private fun getIntegerInput(scanner: Scanner): Int {
    return try {
        scanner.nextLine().toInt()
    } catch (exception: NumberFormatException) {
        -1
    }
}

fun getClientBySession(scanner: Scanner): IGClient? {
    val sessions = File("sessions").list()
    if (sessions.isNullOrEmpty()) return null
    sessions.forEachIndexed { index, name -> println("$index. $name") }
    val userInput = getIntegerInput(scanner)
    val clientFile = File("sessions/${sessions[userInput]}/client.ser")
    val cookieFile = File("sessions/${sessions[userInput]}/cookie.ser")
    println("Login success!")
    return IGClient.deserialize(clientFile, cookieFile)
}

private fun handleUserPostsFetcher(scanner: Scanner, client: IGClient) {
    println("Please input desired instagram username to see posts: ")
    val targetUsername = scanner.nextLine()
    val newData = RequestHelper(client).getUserFeed(targetUsername)
    println("${newData.size} posts have been fetched, show 10 them? (y/n)")
    val answer = scanner.nextLine()
    if (answer.lowercase() == "y") println(newData.take(10).pretty())
}

private fun getClientByUsernamePassword(scanner: Scanner): IGClient {
    println("Please input instagram username: ")
    val username = scanner.nextLine()
    println("Please input instagram password: ")
    val password = scanner.nextLine()

    val client = LoginHelper(username, password).logInWithChallenge { _, _ -> println("Login success!") }

    if (client.isLoggedIn) {
        val request = client.sendRequest(AccountsCurrentUserRequest()).handleAsync { response, _ ->
            val currentUser = response.user
            println("Current user full name is: ${currentUser.full_name} and username is: ${currentUser.username}")

            createSessionFiles(client, username)
        }
        loading(request)
    } else println("Login failed!")
    return client
}

fun createSessionFiles(client: IGClient, username: String) {
    File("sessions/$username").mkdirs()
    val clientFile = File("sessions/$username/client.ser")
    val cookieFile = File("sessions/$username/cookie.ser")
    client.serialize(clientFile, cookieFile)
}

fun loading(completableFuture: CompletableFuture<Unit?>, cycleWaitTime: Long = 200) {
    while (!completableFuture.isDone) {
        print("▮")
        Thread.sleep(cycleWaitTime)
    }
}

suspend fun loadingAsync(cycleWaitTime: Long = 200, cycles: Int = 100) = CoroutineScope(Dispatchers.Main).launch {
    repeat(cycles) {
        print("▮")
        delay(cycleWaitTime)
    }
}