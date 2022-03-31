---
title: Scalr
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)



# Scalr



[jvm]\
object [Scalr](index.html)

Class used to implement performant, high-quality and intelligent image scaling and manipulation algorithms in native Java 2D.



This class utilizes the Java2D "best practices" for image manipulation, ensuring that all operations (even most user-provided [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) s) are hardware accelerated if provided by the platform and host-VM.



<h3>Image Quality</h3> This class implements a few different methods for scaling an image, providing either the best-looking result, the fastest result or a balanced result between the two depending on the scaling hint provided (see [Method](-method/index.html)).



This class also implements an optimized version of the incremental scaling algorithm presented by Chris Campbell in his [Perils of



- 
   Image.getScaledInstance()](http://today.java   .net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html) article in order to give the best-looking image resize results (e.g. generating thumbnails that aren't blurry or jagged).




The results generated by imgscalr using this method, as compared to a single [RenderingHints.VALUE_INTERPOLATION_BICUBIC](https://docs.oracle.com/javase/8/docs/api/java/awt/RenderingHints.html#VALUE_INTERPOLATION_BICUBIC--) scale operation look much better, especially when using the [Method.ULTRA_QUALITY](-method/-u-l-t-r-a_-q-u-a-l-i-t-y/index.html) method.



Only when scaling using the [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) method will this class look at the size of the image before selecting an approach to scaling the image. If [Method.QUALITY](-method/-q-u-a-l-i-t-y/index.html) is specified, the best-looking algorithm possible is always used.



Minor modifications are made to Campbell's original implementation in the form of:



1. 
   Instead of accepting a user-supplied interpolation method, [RenderingHints.VALUE_INTERPOLATION_BICUBIC](https://docs.oracle.com/javase/8/docs/api/java/awt/RenderingHints.html#VALUE_INTERPOLATION_BICUBIC--) interpolation is always used. This was done after A/B comparison testing with large images down-scaled to thumbnail sizes showed noticeable "blurring" when BILINEAR interpolation was used. Given that Campbell's algorithm is only used in QUALITY mode when down-scaling, it was determined that the user's expectation of a much less blurry picture would require that BICUBIC be the default interpolation in order to meet the QUALITY expectation.
2. 
   After each iteration of the do-while loop that incrementally scales the source image down, an explicit effort is made to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the interim temporary [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) instances created by the algorithm in an attempt to ensure a more complete GC cycle by the VM when cleaning up the temporary instances (this is in addition to disposing of the temporary [Graphics2D](https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html) references as well).
3. 
   Extensive comments have been added to increase readability of the code.
4. 
   Variable names have been expanded to increase readability of the code.




**NOTE**: This class does not call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on any of the *source images* passed in by calling code; it is up to the original caller to dispose of their source images when they are no longer needed so the VM can most efficiently GC them. <h3>Image Proportions</h3> All scaling operations implemented by this class maintain the proportions of the original image unless a mode of [Mode.FIT_EXACT](-mode/-f-i-t_-e-x-a-c-t/index.html) is specified; in which case the orientation and proportion of the source image is ignored and the image is stretched (if necessary) to fit the exact dimensions given.



When not using [Mode.FIT_EXACT](-mode/-f-i-t_-e-x-a-c-t/index.html), in order to maintain the proportionality of the original images, this class implements the following behavior:



1. 
   If the image is LANDSCAPE-oriented or SQUARE, treat the targetWidth as the primary dimension and re-calculate the targetHeight regardless of what is passed in.
2. 
   If image is PORTRAIT-oriented, treat the targetHeight as the primary dimension and re-calculate the targetWidth regardless of what is passed in.
3. 
   If a [Mode](-mode/index.html) value of [Mode.FIT_TO_WIDTH](-mode/-f-i-t_-t-o_-w-i-d-t-h/index.html) or [Mode.FIT_TO_HEIGHT](-mode/-f-i-t_-t-o_-h-e-i-g-h-t/index.html) is passed in to the resize method, the image's orientation is ignored and the scaled image is fit to the preferred dimension by using the value passed in by the user for that dimension and recalculating the other (regardless of image orientation). This is useful, for example, when working with PORTRAIT oriented images that you need to all be the same width or visa-versa (e.g. showing user profile pictures in a directory listing).




<h3>Optimized Image Handling</h3> Java2D provides support for a number of different image types defined as BufferedImage.TYPE_* variables, unfortunately not all image types are supported equally in the Java2D rendering pipeline.



Some more obscure image types either have poor or no support, leading to severely degraded quality and processing performance when an attempt is made by imgscalr to create a scaled instance *of the same type* as the source image. In many cases, especially when applying [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) s, using poorly supported image types can even lead to exceptions or total corruption of the image (e.g. solid black image).



imgscalr specifically accounts for and automatically hands **ALL** of these pain points for you internally by shuffling all images into one of two types:



1. 
   [BufferedImage.TYPE_INT_RGB](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_RGB--)
2. 
   [BufferedImage.TYPE_INT_ARGB](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_ARGB--)




depending on if the source image utilizes transparency or not. This is a recommended approach by the Java2D team for dealing with poorly (or non) supported image types. More can be read about this issue [here](http://www.mail-archive.com/java2d-interest@capra.eng.sun.com/msg05621.html).



This is also the reason we recommend using .apply to apply your own ops to images even if you aren't using imgscalr for anything else. <h3>GIF Transparency</h3> Unfortunately in Java 6 and earlier, support for GIF's [IndexColorModel](https://docs.oracle.com/javase/8/docs/api/java/awt/image/IndexColorModel.html) is sub-par, both in accurate color-selection and in maintaining transparency when moving to an image of type [BufferedImage.TYPE_INT_ARGB](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#TYPE_INT_ARGB--); because of this issue when a GIF image is processed by imgscalr and the result saved as a GIF file (instead of PNG), it is possible to lose the alpha channel of a transparent image or in the case of applying an optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html), lose the entire picture all together in the result (long standing JDK bugs are filed for all of these issues).



imgscalr currently does nothing to work around this manually because it is a defect in the native platform code itself. Fortunately it looks like the issues are half-fixed in Java 7 and any manual workarounds we could attempt internally are relatively expensive, in the form of hand-creating and setting RGB values pixel-by-pixel with a custom [ColorModel](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ColorModel.html) in the scaled image. This would lead to a very measurable negative impact on performance without the caller understanding why.



**Workaround**: A workaround to this issue with all version of Java is to simply save a GIF as a PNG; no change to your code needs to be made except when the image is saved out, e.g. using ImageIO.



When a file type of "PNG" is used, both the transparency and high color quality will be maintained as the PNG code path in Java2D is superior to the GIF implementation.



If the issue with optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s destroying GIF image content is ever fixed in the platform, saving out resulting images as GIFs should suddenly start working.



More can be read about the issue [here](http://gman.eichberger.de/2007/07/transparent-gifs-in-java.html) and [here](http://ubuntuforums.org/archive/index.php/t-1060128.html). <h3>Thread Safety</h3> The [Scalr](index.html) class is **thread-safe** (as all the methods are static); this class maintains no internal state while performing any of the provided operations and is safe to call simultaneously from multiple threads. <h3>Logging</h3> This class implements all its debug logging via the .log method. At this time logging is done directly to System.out via the printf method. This allows the logging to be light weight and easy to capture (every imgscalr log message is prefixed with the .LOG_PREFIX string) while adding no dependencies to the library.



Implementation of logging in this class is as efficient as possible; avoiding any calls to the logger method or passing of arguments if logging is not enabled to avoid the (hidden) cost of constructing the Object[] argument for the varargs-based method call.



#### Author



Riyad Kalla (software@thebuzzmedia.com)



#### Since



1.1



## Types


| Name | Summary |
|---|---|
| [Method](-method/index.html) | [jvm]<br>enum [Method](-method/index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Scalr.Method](-method/index.html)&gt; <br>Used to define the different scaling hints that the algorithm can use. |
| [Mode](-mode/index.html) | [jvm]<br>enum [Mode](-mode/index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Scalr.Mode](-mode/index.html)&gt; <br>Used to define the different modes of resizing that the algorithm can use. |
| [Rotation](-rotation/index.html) | [jvm]<br>enum [Rotation](-rotation/index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Scalr.Rotation](-rotation/index.html)&gt; <br>Used to define the different types of rotations that can be applied to an image during a resize operation. |


## Functions


| Name | Summary |
|---|---|
| [apply](apply.html) | [jvm]<br>fun [apply](apply.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to apply, in the order given, 1 or more [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to a given [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) and return the result. |
| [crop](crop.html) | [jvm]<br>fun [crop](crop.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to crop the given src image from the top-left corner and applying any optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to the result before returning it.<br>[jvm]<br>fun [crop](crop.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), y: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to crop the given src image and apply any optional [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to it before returning the result. |
| [pad](pad.html) | [jvm]<br>fun [pad](pad.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, padding: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to apply padding around the edges of an image using [Color.BLACK](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html#BLACK--) to fill the extra padded space and then return the result.<br>[jvm]<br>fun [pad](pad.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, padding: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), color: [Color](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html)?, vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to apply padding around the edges of an image using the given color to fill the extra padded space and then return the result. [Color](https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html)s using an alpha channel (i.e. transparency) are supported. |
| [resize](resize.html) | [jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to the target width and height and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to the target width and height using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to the target width and height (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.<br>[jvm]<br>fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?<br>Resize a given image (maintaining its original proportion) to the target width and height (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it. |
| [rotate](rotate.html) | [jvm]<br>fun [rotate](rotate.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, rotation: [Scalr.Rotation](-rotation/index.html)?, vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)<br>Used to apply a [Rotation](-rotation/index.html) and then 0 or more [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to a given image and return the result. |


## Properties


| Name | Summary |
|---|---|
| [DEBUG](-d-e-b-u-g.html) | [jvm]<br>const val [DEBUG](-d-e-b-u-g.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Flag used to indicate if debugging output has been enabled by setting the "imgscalr.debug" system property to true. This value will be false if the "imgscalr.debug" system property is undefined or set to false. |
| [DEBUG_PROPERTY_NAME](-d-e-b-u-g_-p-r-o-p-e-r-t-y_-n-a-m-e.html) | [jvm]<br>const val [DEBUG_PROPERTY_NAME](-d-e-b-u-g_-p-r-o-p-e-r-t-y_-n-a-m-e.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>System property name used to define the debug boolean flag. |
| [LOG_PREFIX](-l-o-g_-p-r-e-f-i-x.html) | [jvm]<br>val [LOG_PREFIX](-l-o-g_-p-r-e-f-i-x.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Prefix to every log message this library logs. Using a well-defined prefix helps make it easier both visually and programmatically to scan log files for messages produced by this library. |
| [LOG_PREFIX_PROPERTY_NAME](-l-o-g_-p-r-e-f-i-x_-p-r-o-p-e-r-t-y_-n-a-m-e.html) | [jvm]<br>const val [LOG_PREFIX_PROPERTY_NAME](-l-o-g_-p-r-e-f-i-x_-p-r-o-p-e-r-t-y_-n-a-m-e.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>System property name used to define a custom log prefix. |
| [OP_ANTIALIAS](-o-p_-a-n-t-i-a-l-i-a-s.html) | [jvm]<br>val [OP_ANTIALIAS](-o-p_-a-n-t-i-a-l-i-a-s.html): [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html)<br>A [ConvolveOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ConvolveOp.html) using a very light "blur" kernel that acts like an anti-aliasing filter (softens the image a bit) when applied to an image. |
| [OP_BRIGHTER](-o-p_-b-r-i-g-h-t-e-r.html) | [jvm]<br>val [OP_BRIGHTER](-o-p_-b-r-i-g-h-t-e-r.html): [RescaleOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/RescaleOp.html)<br>A [RescaleOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/RescaleOp.html) used to make any input image 10% brighter. |
| [OP_DARKER](-o-p_-d-a-r-k-e-r.html) | [jvm]<br>val [OP_DARKER](-o-p_-d-a-r-k-e-r.html): [RescaleOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/RescaleOp.html)<br>A [RescaleOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/RescaleOp.html) used to make any input image 10% darker. |
| [OP_GRAYSCALE](-o-p_-g-r-a-y-s-c-a-l-e.html) | [jvm]<br>val [OP_GRAYSCALE](-o-p_-g-r-a-y-s-c-a-l-e.html): [ColorConvertOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ColorConvertOp.html)<br>A [ColorConvertOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ColorConvertOp.html) used to convert any image to a grayscale color palette. |
| [THRESHOLD_BALANCED_SPEED](-t-h-r-e-s-h-o-l-d_-b-a-l-a-n-c-e-d_-s-p-e-e-d.html) | [jvm]<br>const val [THRESHOLD_BALANCED_SPEED](-t-h-r-e-s-h-o-l-d_-b-a-l-a-n-c-e-d_-s-p-e-e-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1600<br>Threshold (in pixels) at which point the scaling operation using the [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) method will decide if a [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) method will be used (if smaller than or equal to threshold) or a [Method.SPEED](-method/-s-p-e-e-d/index.html) method will be used (if larger than threshold). |
| [THRESHOLD_QUALITY_BALANCED](-t-h-r-e-s-h-o-l-d_-q-u-a-l-i-t-y_-b-a-l-a-n-c-e-d.html) | [jvm]<br>const val [THRESHOLD_QUALITY_BALANCED](-t-h-r-e-s-h-o-l-d_-q-u-a-l-i-t-y_-b-a-l-a-n-c-e-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 800<br>Threshold (in pixels) at which point the scaling operation using the [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) method will decide if a [Method.QUALITY](-method/-q-u-a-l-i-t-y/index.html) method will be used (if smaller than or equal to threshold) or a [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) method will be used (if larger than threshold). |
