---
title: rotate
---
//[InstaKiller](../../../index.html)/[io.github.instakiller.utils](../index.html)/[Scalr](index.html)/[rotate](rotate.html)



# rotate



[jvm]\
fun [rotate](rotate.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, rotation: [Scalr.Rotation](-rotation/index.html)?, vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to apply a [Rotation](-rotation/index.html) and then 0 or more [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to a given image and return the result.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing src rotated by the given amount and any optional ops applied to it.



## See also


jvm

| | |
|---|---|
| [io.github.instakiller.utils.Scalr.Rotation](-rotation/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will have the rotation applied to it. |
| rotation | The rotation that will be applied to the image. |
| ops | Zero or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if rotation is null. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |



