package yamin

import com.github.instagram4j.instagram4j.models.user.Profile
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest
import yamin.utils.JsonUtils.pretty
import yamin.utils.LoginHelper


fun main() {
    val client = LoginHelper("graph.agency", "137426").logInWithChallenge()

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