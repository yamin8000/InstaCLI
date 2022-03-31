---
title: apply
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[apply](apply.html)



# apply



[jvm]\
fun [apply](apply.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)



Used to apply, in the order given, 1 or more [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to a given [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) and return the result.



**Feature**: This implementation works around [a



- 
   decade-old JDK bug](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4965606) that can cause a [RasterFormatException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/RasterFormatException.html) when applying a perfectly valid [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to images.




**Feature**: This implementation also works around [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s failing to apply and throwing [ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html)s when run against a src image type that is poorly supported. Unfortunately using ImageIO and standard Java methods to load images provides no consistency in getting images in well-supported formats. This method automatically accounts and corrects for all those problems (if necessary).



It is recommended you always use this method to apply any [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s instead of relying on directly using the [BufferedImageOp.filter](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html#filter-java.awt.image.BufferedImage-java.awt.image.BufferedImage-) method.



**Performance**: Not all [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s are hardware accelerated operations, but many of the most popular (like [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html)) are. For more information on if your image op is hardware accelerated or not, check the source code of the underlying JDK class that actually executes the Op code, [sun.awt.image.ImagingLib](http://www.docjar.com/html/api/sun/awt/image/ImagingLib.java.html).



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) that represents the src with all the given operations applied to it.



## Parameters


jvm

| | |
|---|---|
| src | The image that will have the ops applied to it. |
| ops | 1 or more ops to apply to the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if ops is null or empty. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |



