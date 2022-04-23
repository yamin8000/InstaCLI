---
title: Utility
---
//[InstaKiller](../../../index.html)/[io.github.instakiller.utils](../index.html)/[Utility](index.html)



# Utility



[jvm]\
object [Utility](index.html)

Utility class containing various utility functions



## Functions


| Name | Summary |
|---|---|
| [actionPair](action-pair.html) | [jvm]<br>fun &lt;[U](action-pair.html)&gt; [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[U](action-pair.html)&gt;.[actionPair](action-pair.html)(): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[U](action-pair.html)?&gt;<br>returns [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109) instance containing [U](action-pair.html)/IGResponse and [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) related to action for [U](action-pair.html) |
| [getName](get-name.html) | [jvm]<br>fun [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html).[getName](get-name.html)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>return name of the given url |
| [isoTime](iso-time.html) | [jvm]<br>fun [isoTime](iso-time.html)(localDateTime: [LocalDateTime](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>returns [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) form of [localDateTime](iso-time.html) in ISO 8601 format |
| [isoTimeOfEpoch](iso-time-of-epoch.html) | [jvm]<br>fun [isoTimeOfEpoch](iso-time-of-epoch.html)(epoch: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>returns [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) form of [epoch](iso-time-of-epoch.html) in ISO 8601 format with [ZoneOffset.UTC](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneOffset.html#UTC--) |
| [now](now.html) | [jvm]<br>fun [now](now.html)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>returns the current time in ISO 8601 format |
| [pair](pair.html) | [jvm]<br>fun &lt;[T](pair.html) : IGResponse&gt; [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[T](pair.html)&gt;.[pair](pair.html)(): [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[T](pair.html)?&gt;<br>returns [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109) instance containing [T](pair.html)/IGResponse and [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) related to request for [T](pair.html) |
| [requirePositiveLimit](require-positive-limit.html) | [jvm]<br>fun [requirePositiveLimit](require-positive-limit.html)(limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [solo](solo.html) | [jvm]<br>fun &lt;[T](solo.html)&gt; [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[T](solo.html)?&gt;.[solo](solo.html)(success: ([T](solo.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), failed: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = "Error")<br>This method will notify the callback with a non-null value if it is not null, otherwise, If data is null error message is shown to user. |

