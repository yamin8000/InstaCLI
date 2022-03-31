---
title: LOG_PREFIX
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[LOG_PREFIX](-l-o-g_-p-r-e-f-i-x.html)



# LOG_PREFIX



[jvm]\
val [LOG_PREFIX](-l-o-g_-p-r-e-f-i-x.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)



Prefix to every log message this library logs. Using a well-defined prefix helps make it easier both visually and programmatically to scan log files for messages produced by this library.



This property can be set on startup with:<br></br>-Dimgscalr.logPrefix=&lt;YOUR PREFIX HERE&gt;  *  or by calling [System.setProperty](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#setProperty-kotlin.String-kotlin.String-) to set a new property value for .LOG_PREFIX_PROPERTY_NAME before this class is loaded.



Default value is "[imgscalr] " (including the space).




