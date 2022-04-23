---
title: OP_ANTIALIAS
---
//[InstaKiller](../../../index.html)/[io.github.instakiller.utils](../index.html)/[Scalr](index.html)/[OP_ANTIALIAS](-o-p_-a-n-t-i-a-l-i-a-s.html)



# OP_ANTIALIAS



[jvm]\
val [OP_ANTIALIAS](-o-p_-a-n-t-i-a-l-i-a-s.html): [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html)



A [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html) using a very light "blur" kernel that acts like an anti-aliasing filter (softens the image a bit) when applied to an image.



A common request by users of the library was that they wished to "soften" resulting images when scaling them down drastically. After quite a bit of A/B testing, the kernel used by this Op was selected as the closest match for the target which was the softer results from the deprecated [AreaAveragingScaleFilter](https://docs.oracle.com/javase/8/docs/api/java/awt/image/AreaAveragingScaleFilter.html) (which is used internally by the deprecated Image.getScaledInstance method in the JDK that imgscalr is meant to replace).



This ConvolveOp uses a 3x3 kernel with the values:

<table cellpadding="4" border="1">
<tr>
<td>.0f</td>
<td>.08f</td>
<td>.0f</td>
</tr> *
<tr>
<td>.08f</td>
<td>.68f</td>
<td>.08f</td>
</tr> *
<tr>
<td>.0f</td>
<td>.08f</td>
<td>.0f</td>
</tr> *
</table> *

For those that have worked with ConvolveOps before, this Op uses the [ConvolveOp.EDGE_NO_OP](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html#EDGE_NO_OP--) instruction to not process the pixels along the very edge of the image (otherwise EDGE_ZERO_FILL would create a black-border around the image). If you have not worked with a ConvolveOp before, it just means this default OP will "do the right thing" and not give you garbage results.



This ConvolveOp uses no [RenderingHints](https://docs.oracle.com/javase/8/docs/api/java/awt/RenderingHints.html) values as internally the [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html) class only uses hints when doing a color conversion between the source and destination [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) targets. imgscalr allows the [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html) to create its own destination image every time, so no color conversion is ever needed and thus no hints. <h3>Performance</h3> Use of this (and other) [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html)s are hardware accelerated when possible. For more information on if your image op is hardware accelerated or not, check the source code of the underlying JDK class that actually executes the Op code, [sun.awt.image.ImagingLib](http://www.docjar.com/html/api/sun/awt/image/ImagingLib.java.html). <h3>Known Issues</h3> In all versions of Java (tested up to Java 7 preview Build 131), running this op against a GIF with transparency and attempting to save the resulting image as a GIF results in a corrupted/empty file. The file must be saved out as a PNG to maintain the transparency.



#### Since



3.0




