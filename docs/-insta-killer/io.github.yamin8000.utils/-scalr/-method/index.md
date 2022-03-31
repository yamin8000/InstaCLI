---
title: Method
---
//[InstaKiller](../../../../index.html)/[io.github.yamin8000.utils](../../index.html)/[Scalr](../index.html)/[Method](index.html)



# Method



[jvm]\
enum [Method](index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Scalr.Method](index.html)&gt; 

Used to define the different scaling hints that the algorithm can use.



#### Author



Riyad Kalla (software@thebuzzmedia.com)



#### Since



1.1



## Entries


| | |
|---|---|
| [ULTRA_QUALITY](-u-l-t-r-a_-q-u-a-l-i-t-y/index.html) | [jvm]<br>[ULTRA_QUALITY](-u-l-t-r-a_-q-u-a-l-i-t-y/index.html)()<br>Used to indicate that the scaling implementation should go above and beyond the work done by [Method.QUALITY](-q-u-a-l-i-t-y/index.html) to make the image look exceptionally good at the cost of more processing time. This is especially evident when generating thumbnails of images that look jagged with some of the other [Method](index.html)s (even [Method.QUALITY](-q-u-a-l-i-t-y/index.html)). |
| [QUALITY](-q-u-a-l-i-t-y/index.html) | [jvm]<br>[QUALITY](-q-u-a-l-i-t-y/index.html)()<br>Used to indicate that the scaling implementation should do everything it can to create as nice of a result as possible. This approach is most important for smaller pictures (800px or smaller) and less important for larger pictures as the difference between this method and the SPEED method become less and less noticeable as the source-image size increases. Using the AUTOMATIC method will automatically prefer the QUALITY method when scaling an image down below 800px in size. |
| [BALANCED](-b-a-l-a-n-c-e-d/index.html) | [jvm]<br>[BALANCED](-b-a-l-a-n-c-e-d/index.html)()<br>Used to indicate that the scaling implementation should use a scaling operation balanced between SPEED and QUALITY. Sometimes SPEED looks too low quality to be useful (e.g. text can become unreadable when scaled using SPEED) but using QUALITY mode will increase the processing time too much. This mode provides a "better than SPEED" quality in a "less than QUALITY" amount of time. |
| [SPEED](-s-p-e-e-d/index.html) | [jvm]<br>[SPEED](-s-p-e-e-d/index.html)()<br>Used to indicate that the scaling implementation should scale as fast as possible and return a result. For smaller images (800px in size) this can result in noticeable aliasing but it can be a few magnitudes times faster than using the QUALITY method. |
| [AUTOMATIC](-a-u-t-o-m-a-t-i-c/index.html) | [jvm]<br>[AUTOMATIC](-a-u-t-o-m-a-t-i-c/index.html)()<br>Used to indicate that the scaling implementation should decide which method to use in order to get the best looking scaled image in the least amount of time. |


## Properties


| Name | Summary |
|---|---|
| [name](../-rotation/-c-w_90/index.html#-372974862%2FProperties%2F863300109) | [jvm]<br>val [name](../-rotation/-c-w_90/index.html#-372974862%2FProperties%2F863300109): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../-rotation/-c-w_90/index.html#-739389684%2FProperties%2F863300109) | [jvm]<br>val [ordinal](../-rotation/-c-w_90/index.html#-739389684%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

