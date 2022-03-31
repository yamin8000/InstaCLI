---
title: solo
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Utility](index.html)/[solo](solo.html)



# solo



[jvm]\
fun &lt;[T](solo.html)&gt; [Dyad](../../io.github.yamin8000/index.html#1921977161%2FClasslikes%2F863300109)&lt;[T](solo.html)?&gt;.[solo](solo.html)(success: ([T](solo.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), failed: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = "Error")



This method will notify the callback with a non-null value if it is not null, otherwise, If data is null error message is shown to user.



## Parameters


jvm

| | |
|---|---|
| T | data type |
| message | error message |
| success | callback for injecting non-null data |
| failed | callback for showing error message |




