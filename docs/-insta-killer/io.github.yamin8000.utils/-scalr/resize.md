---
title: resize
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[resize](resize.html)



# resize



[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A scaling method of [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) and mode of [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) are used.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| targetSize | The target width and height (square) that you wish the image to fit within. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetSize is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A mode of [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is used.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Method](-method/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| scalingMethod | The method used for scaling the image; preferring speed to quality or a balance of both. |
| targetSize | The target width and height (square) that you wish the image to fit within. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetSize is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A scaling method of [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) is used.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Mode](-mode/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| resizeMode | Used to indicate how imgscalr should calculate the final target size for the image, either fitting the image to the given width ([Mode.FIT_TO_WIDTH](-mode/-f-i-t_-t-o_-w-i-d-t-h/index.html)) or fitting the image to the given height ([Mode.FIT_TO_HEIGHT](-mode/-f-i-t_-t-o_-h-e-i-g-h-t/index.html)). If [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is passed in, imgscalr will calculate proportional dimensions for the scaled image based on its orientation (landscape, square or portrait). Unless you have very specific size requirements, most of the time you just want to use [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) to "do the right thing". |
| targetSize | The target width and height (square) that you wish the image to fit within. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetSize is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to a width and height no bigger than targetSize (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Method](-method/index.html) |  |
| [io.github.yamin8000.utils.Scalr.Mode](-mode/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| scalingMethod | The method used for scaling the image; preferring speed to quality or a balance of both. |
| resizeMode | Used to indicate how imgscalr should calculate the final target size for the image, either fitting the image to the given width ([Mode.FIT_TO_WIDTH](-mode/-f-i-t_-t-o_-w-i-d-t-h/index.html)) or fitting the image to the given height ([Mode.FIT_TO_HEIGHT](-mode/-f-i-t_-t-o_-h-e-i-g-h-t/index.html)). If [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is passed in, imgscalr will calculate proportional dimensions for the scaled image based on its orientation (landscape, square or portrait). Unless you have very specific size requirements, most of the time you just want to use [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) to "do the right thing". |
| targetSize | The target width and height (square) that you wish the image to fit within. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetSize is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to the target width and height and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A scaling method of [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) and mode of [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) are used.



**TIP**: See the class description to understand how this class handles recalculation of the targetWidth or targetHeight depending on the image's orientation in order to maintain the original proportion.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| targetWidth | The target width that you wish the image to have. |
| targetHeight | The target height that you wish the image to have. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetWidth is < 0 or if targetHeight is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to the target width and height using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A mode of [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is used.



**TIP**: See the class description to understand how this class handles recalculation of the targetWidth or targetHeight depending on the image's orientation in order to maintain the original proportion.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Method](-method/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| scalingMethod | The method used for scaling the image; preferring speed to quality or a balance of both. |
| targetWidth | The target width that you wish the image to have. |
| targetHeight | The target height that you wish the image to have. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetWidth is < 0 or if targetHeight is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to the target width and height (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



A scaling method of [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) is used.



**TIP**: See the class description to understand how this class handles recalculation of the targetWidth or targetHeight depending on the image's orientation in order to maintain the original proportion.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Mode](-mode/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| resizeMode | Used to indicate how imgscalr should calculate the final target size for the image, either fitting the image to the given width ([Mode.FIT_TO_WIDTH](-mode/-f-i-t_-t-o_-w-i-d-t-h/index.html)) or fitting the image to the given height ([Mode.FIT_TO_HEIGHT](-mode/-f-i-t_-t-o_-h-e-i-g-h-t/index.html)). If [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is passed in, imgscalr will calculate proportional dimensions for the scaled image based on its orientation (landscape, square or portrait). Unless you have very specific size requirements, most of the time you just want to use [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) to "do the right thing". |
| targetWidth | The target width that you wish the image to have. |
| targetHeight | The target height that you wish the image to have. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetWidth is < 0 or if targetHeight is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |




[jvm]\
fun [resize](resize.html)(src: [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?, scalingMethod: [Scalr.Method](-method/index.html)?, resizeMode: [Scalr.Mode](-mode/index.html)?, targetWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), targetHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg ops: [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)?): [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)?



Resize a given image (maintaining its original proportion) to the target width and height (or fitting the image to the given WIDTH or HEIGHT explicitly, depending on the [Mode](-mode/index.html) specified) using the given scaling method and apply the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s (if any) to the result before returning it.



**TIP**: See the class description to understand how this class handles recalculation of the targetWidth or targetHeight depending on the image's orientation in order to maintain the original proportion.



**TIP**: This operation leaves the original src image unmodified. If the caller is done with the src image after getting the result of this operation, remember to call [BufferedImage.flush](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html#flush--) on the src to free up native resources and make it easier for the GC to collect the unused image.



#### Return



a new [BufferedImage](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html) representing the scaled src image.



## See also


jvm

| | |
|---|---|
| [io.github.yamin8000.utils.Scalr.Method](-method/index.html) |  |
| [io.github.yamin8000.utils.Scalr.Mode](-mode/index.html) |  |



## Parameters


jvm

| | |
|---|---|
| src | The image that will be scaled. |
| scalingMethod | The method used for scaling the image; preferring speed to quality or a balance of both. |
| resizeMode | Used to indicate how imgscalr should calculate the final target size for the image, either fitting the image to the given width ([Mode.FIT_TO_WIDTH](-mode/-f-i-t_-t-o_-w-i-d-t-h/index.html)) or fitting the image to the given height ([Mode.FIT_TO_HEIGHT](-mode/-f-i-t_-t-o_-h-e-i-g-h-t/index.html)). If [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) is passed in, imgscalr will calculate proportional dimensions for the scaled image based on its orientation (landscape, square or portrait). Unless you have very specific size requirements, most of the time you just want to use [Mode.AUTOMATIC](-mode/-a-u-t-o-m-a-t-i-c/index.html) to "do the right thing". |
| targetWidth | The target width that you wish the image to have. |
| targetHeight | The target height that you wish the image to have. |
| ops | 0 or more optional image operations (e.g. sharpen, blur, etc.) that can be applied to the final result before returning the image. |



## Throws


| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if targetWidth is < 0 or if targetHeight is < 0. |
| [java.awt.image.ImagingOpException](https://docs.oracle.com/javase/8/docs/api/java/awt/image/ImagingOpException.html) | if one of the given [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s fails to apply. These exceptions bubble up from the inside of most of the [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html) implementations and are explicitly defined on the imgscalr API to make it easier for callers to catch the exception (if they are passing along optional ops to be applied). imgscalr takes detailed steps to avoid the most common pitfalls that will cause [BufferedImageOp](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImageOp.html)s to fail, even when using straight forward JDK-image operations. |



