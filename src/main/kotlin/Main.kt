package yamin

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler
import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import com.github.instagram4j.instagram4j.responses.accounts.LoginResponse
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils
import java.util.*
import java.util.concurrent.Callable


fun main() {
    val scanner = Scanner(System.`in`)

    val inputCode = Callable {
        print("Please input code: ")
        scanner.nextLine()
    }


    val challengeHandler = LoginHandler { client: IGClient?, response: LoginResponse? ->
        IGChallengeUtils.resolveChallenge(
            client!!,
            response!!,
            inputCode
        )
    }

    val client = IGClient.builder()
        .username("graph.agency")
        .password("137426")
        .onChallenge(challengeHandler)
        .login()

//    try {
//        client.actions().timeline().uploadPhoto(File("clean.jpg"), "asdasda")
//            .thenAccept { println("hello") }.join()
//    } catch (exception: Exception) {
//        println(exception.stackTraceToString())
//    }


    //val data = client.sendRequest(FeedUserRequest(45250133777)).join()
    //println(data.items.first().pretty())
    //val firstId = data.items.first().id
    //val likers = client.sendRequest(MediaGetLikersRequest(firstId)).join()
    //println(likers.users.pretty())
    //val commenters = client.sendRequest(MediaGetCommentsRequest(firstId)).join()
    //println(commenters.comments.pretty())
    //val count = client.sendRequest(UsersInfoRequest(7995957172)).join().user.follower_count
    //println("count $count")
    val myFollowers = mutableListOf<Profile>()
    var nextMaxId: String? = ""
    while (nextMaxId != null) {
        val friendshipsFeedsRequest =
            FriendshipsFeedsRequest(7995957172, FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, nextMaxId)
        val feedUsersResponse = client.sendRequest(friendshipsFeedsRequest).join()
        nextMaxId = feedUsersResponse.next_max_id
        //println(followers.users.first())
        myFollowers.addAll(feedUsersResponse.users)
        Thread.sleep(1000)
    }
    println(myFollowers.pretty())
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

fun Any.pretty(): String {
    val mapper = ObjectMapper()
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
}
