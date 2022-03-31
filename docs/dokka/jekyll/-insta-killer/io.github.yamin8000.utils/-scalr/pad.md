---
title: pad
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[pad](pad.html)



# pad



[jvm]\
fun [pad](pad.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, padding: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to apply padding around the edges of an image using [Color.BLACK](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html#BLACK--) to fill the extra padded space and then return the result.



The amount of padding specified is applied to all sides; more specifically, a padding of 2 would add 2 extra pixels of space (filled by the given color) on the top, bottom, left and right sides of the resulting image causing the result to be 4 pixels wider and 4 pixels taller than the src image.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing src with the given padding applied to it.



## Parameters


jvm

| | |
|---|---|
| src | The image the padding will be added to. |
| padding | The number of pixels of padding to add to each side in the resulting image. If this value is 0 then src is returned unmodified. |
| ops | 0 or more ops to apply to the image. If null or empty then src is return unmodified. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if padding is <1. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [pad](pad.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, padding: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), color: [Color](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html)?, vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to apply padding around the edges of an image using the given color to fill the extra padded space and then return the result. [Color](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html)s using an alpha channel (i.e. transparency) are supported.



The amount of padding specified is applied to all sides; more specifically, a padding of 2 would add 2 extra pixels of space (filled by the given color) on the top, bottom, left and right sides of the resulting image causing the result to be 4 pixels wider and 4 pixels taller than the src image.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing src with the given padding applied to it.



## Parameters


jvm

| | |
|---|---|
| src | The image the padding will be added to. |
| padding | The number of pixels of padding to add to each side in the resulting image. If this value is 0 then src is returned unmodified. |
| color | The color to fill the padded space with. [Color](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html)s using an alpha channel (i.e. transparency) are supported. |
| ops | 0 or more ops to apply to the image. If null or empty then src is return unmodified. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if color is null. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |



