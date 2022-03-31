---
title: ConsoleHelper
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.console](../index.html)/[ConsoleHelper](index.html)



# ConsoleHelper



[jvm]\
object [ConsoleHelper](index.html)

Helper class for console input/output



## Functions


| Name | Summary |
|---|---|
| [pressEnterToContinue](press-enter-to-continue.html) | [jvm]<br>fun [pressEnterToContinue](press-enter-to-continue.html)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "continue...")<br>Prompts the user to press enter to continue |
| [readBoolean](read-boolean.html) | [jvm]<br>fun [readBoolean](read-boolean.html)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Prompts the user for a [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) input with the given optional [message](read-boolean.html), and eventually return the input as [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [readCleanLine](read-clean-line.html) | [jvm]<br>fun [readCleanLine](read-clean-line.html)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Reads a line from input and [String.trim](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/index.html) it, if the input is null then returns an empty [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [readInteger](read-integer.html) | [jvm]<br>fun [readInteger](read-integer.html)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, range: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)? = null): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Prompts the user for an [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html) input with the given optional [message](read-integer.html), the number must be between [range](read-integer.html) if included otherwise any [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html) is acceptable. Eventually return the input as [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readMultipleStrings](read-multiple-strings.html) | [jvm]<br>fun [readMultipleStrings](read-multiple-strings.html)(field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Prompts the user for entering multiple [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values, and eventually return a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [readSingleString](read-single-string.html) | [jvm]<br>fun [readSingleString](read-single-string.html)(field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Prompts the user for a single [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value, and eventually return the input as [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

