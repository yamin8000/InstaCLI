---
title: LiveStreamHelper
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.helpers](../index.html)/[LiveStreamHelper](index.html)



# LiveStreamHelper



[jvm]\
class [LiveStreamHelper](index.html)(igClient: IGClient)



## Functions


| Name | Summary |
|---|---|
| [addComment](add-comment.html) | [jvm]<br>fun [addComment](add-comment.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), comment: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;LiveBroadcastCommentResponse?&gt; |
| [createLive](create-live.html) | [jvm]<br>fun [createLive](create-live.html)(): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;LiveCreateResponse?, [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?&gt; |
| [getComments](get-comments.html) | [jvm]<br>fun [getComments](get-comments.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Comment&gt;?&gt; |
| [getLiveInfo](get-live-info.html) | [jvm]<br>fun [getLiveInfo](get-live-info.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;IGResponse?&gt; |
| [getViewerList](get-viewer-list.html) | [jvm]<br>fun [getViewerList](get-viewer-list.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Profile&gt;?&gt; |
| [startLive](start-live.html) | [jvm]<br>fun [startLive](start-live.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), sendNotification: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;LiveStartResponse?&gt; |
| [stopLive](stop-live.html) | [jvm]<br>fun [stopLive](stop-live.html)(broadcastId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;IGResponse?&gt; |

