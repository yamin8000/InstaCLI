---
title: Mode
---
//[InstaKiller](../../../../index.html)/[io.github.yamin8000.utils](../../index.html)/[Scalr](../index.html)/[Mode](index.html)



# Mode



[jvm]\
enum [Mode](index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Scalr.Mode](index.html)&gt; 

Used to define the different modes of resizing that the algorithm can use.



#### Author



Riyad Kalla (software@thebuzzmedia.com)



#### Since



3.1



## Entries


| | |
|---|---|
| [FIT_TO_HEIGHT](-f-i-t_-t-o_-h-e-i-g-h-t/index.html) | [jvm]<br>[FIT_TO_HEIGHT](-f-i-t_-t-o_-h-e-i-g-h-t/index.html)()<br>Used to indicate that the scaling implementation should calculate dimensions for the resultant image that best-fit within the given height, regardless of the orientation of the image. |
| [FIT_TO_WIDTH](-f-i-t_-t-o_-w-i-d-t-h/index.html) | [jvm]<br>[FIT_TO_WIDTH](-f-i-t_-t-o_-w-i-d-t-h/index.html)()<br>Used to indicate that the scaling implementation should calculate dimensions for the resultant image that best-fit within the given width, regardless of the orientation of the image. |
| [BEST_FIT_BOTH](-b-e-s-t_-f-i-t_-b-o-t-h/index.html) | [jvm]<br>[BEST_FIT_BOTH](-b-e-s-t_-f-i-t_-b-o-t-h/index.html)()<br>Used to indicate that the scaling implementation should calculate dimensions for the largest image that fit within the bounding box, without cropping or distortion, retaining the original proportions. |
| [FIT_EXACT](-f-i-t_-e-x-a-c-t/index.html) | [jvm]<br>[FIT_EXACT](-f-i-t_-e-x-a-c-t/index.html)()<br>Used to fit the image to the exact dimensions given regardless of the image's proportions. If the dimensions are not proportionally correct, this will introduce vertical or horizontal stretching to the image. |
| [AUTOMATIC](-a-u-t-o-m-a-t-i-c/index.html) | [jvm]<br>[AUTOMATIC](-a-u-t-o-m-a-t-i-c/index.html)()<br>Used to indicate that the scaling implementation should calculate dimensions for the resultant image by looking at the image's orientation and generating proportional dimensions that best fit into the target width and height given |


## Properties


| Name | Summary |
|---|---|
| [name](../-rotation/-c-w_90/index.html#-372974862%2FProperties%2F863300109) | [jvm]<br>val [name](../-rotation/-c-w_90/index.html#-372974862%2FProperties%2F863300109): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../-rotation/-c-w_90/index.html#-739389684%2FProperties%2F863300109) | [jvm]<br>val [ordinal](../-rotation/-c-w_90/index.html#-739389684%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

