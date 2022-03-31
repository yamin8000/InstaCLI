---
title: crop
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[crop](crop.html)



# crop



[jvm]\
fun [crop](crop.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to crop the given src image from the top-left corner and applying any optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to the result before returning it.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the cropped region of the src image with any optional operations applied to it.



## Parameters


jvm

| | |
|---|---|
| src | The image to crop. |
| width | The width of the bounding cropping box. |
| height | The height of the bounding cropping box. |
| ops | 0 or more ops to apply to the image. If null or empty then src is return unmodified. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if any coordinates of the bounding crop box is invalid within the bounds of the src image (e.g. negative or too big). |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [crop](crop.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), y: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to crop the given src image and apply any optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to it before returning the result.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the cropped region of the src image with any optional operations applied to it.



## Parameters


jvm

| | |
|---|---|
| src | The image to crop. |
| x | The x-coordinate of the top-left corner of the bounding box used for cropping. |
| y | The y-coordinate of the top-left corner of the bounding box used for cropping. |
| width | The width of the bounding cropping box. |
| height | The height of the bounding cropping box. |
| ops | 0 or more ops to apply to the image. If null or empty then src is return unmodified. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if any coordinates of the bounding crop box is invalid within the bounds of the src image (e.g. negative or too big). |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |



