---
title: DEBUG
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[DEBUG](-d-e-b-u-g.html)



# DEBUG



[jvm]\
const val [DEBUG](-d-e-b-u-g.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false



Flag used to indicate if debugging output has been enabled by setting the "imgscalr.debug" system property to true. This value will be false if the "imgscalr.debug" system property is undefined or set to false.



This property can be set on startup with:<br></br>-Dimgscalr.debug=true  *  or by calling [System.setProperty](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#setProperty-kotlin.String-kotlin.String-) to set a new property value for .DEBUG_PROPERTY_NAME before this class is loaded.



Default value is false.




