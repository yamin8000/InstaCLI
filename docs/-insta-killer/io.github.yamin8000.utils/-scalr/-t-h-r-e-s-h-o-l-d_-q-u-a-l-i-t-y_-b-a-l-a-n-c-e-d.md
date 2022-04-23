---
title: THRESHOLD_QUALITY_BALANCED
---
//[InstaKiller](../../../index.html)/[io.github.instakiller.utils](../index.html)/[Scalr](index.html)/[THRESHOLD_QUALITY_BALANCED](-t-h-r-e-s-h-o-l-d_-q-u-a-l-i-t-y_-b-a-l-a-n-c-e-d.html)



# THRESHOLD_QUALITY_BALANCED



[jvm]\
const val [THRESHOLD_QUALITY_BALANCED](-t-h-r-e-s-h-o-l-d_-q-u-a-l-i-t-y_-b-a-l-a-n-c-e-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 800



Threshold (in pixels) at which point the scaling operation using the [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) method will decide if a [Method.QUALITY](-method/-q-u-a-l-i-t-y/index.html) method will be used (if smaller than or equal to threshold) or a [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) method will be used (if larger than threshold).



The bigger the image is being scaled to, the less noticeable degradations in the image becomes and the faster algorithms can be selected.



The value of this threshold (800) was chosen after visual, by-hand, A/B testing between different types of images scaled with this library; both photographs and screenshots. It was determined that images below this size need to use a [Method.QUALITY](-method/-q-u-a-l-i-t-y/index.html) scale method to look decent in most all cases while using the faster [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) method for images bigger than this threshold showed no noticeable degradation over a QUALITY scale.




