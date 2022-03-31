---
title: FriendsHelper
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.helpers](../index.html)/[FriendsHelper](index.html)



# FriendsHelper



[jvm]\
class [FriendsHelper](index.html)(igClient: IGClient)



## Functions


| Name | Summary |
|---|---|
| [getFollowers](get-followers.html) | [jvm]<br>fun [getFollowers](get-followers.html)(username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MAX_COUNT, nextMaxId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Profile&gt;?, [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?&gt; |
| [getFollowing](get-following.html) | [jvm]<br>fun [getFollowing](get-following.html)(username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MAX_COUNT, nextMaxId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Profile&gt;?, [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?&gt; |
| [getFriends](get-friends.html) | [jvm]<br>fun [getFriends](get-friends.html)(username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), friendsType: FriendshipsFeedsRequest.FriendshipsFeeds, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = MAX_COUNT, nextMaxId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Profile&gt;?&gt; |
| [getFriendsPaged](get-friends-paged.html) | [jvm]<br>fun [getFriendsPaged](get-friends-paged.html)(username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), friendsType: FriendshipsFeedsRequest.FriendshipsFeeds, nextMaxId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Profile?&gt;?&gt;, [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt; |

