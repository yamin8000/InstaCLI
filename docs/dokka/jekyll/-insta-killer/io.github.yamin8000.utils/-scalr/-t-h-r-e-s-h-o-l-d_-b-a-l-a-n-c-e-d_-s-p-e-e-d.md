---
title: THRESHOLD_BALANCED_SPEED
---
//[InstaKiller](../../../index.html)/[io.github.yamin8000.utils](../index.html)/[Scalr](index.html)/[THRESHOLD_BALANCED_SPEED](-t-h-r-e-s-h-o-l-d_-b-a-l-a-n-c-e-d_-s-p-e-e-d.html)



# THRESHOLD_BALANCED_SPEED



[jvm]\
const val [THRESHOLD_BALANCED_SPEED](-t-h-r-e-s-h-o-l-d_-b-a-l-a-n-c-e-d_-s-p-e-e-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1600



Threshold (in pixels) at which point the scaling operation using the [Method.AUTOMATIC](-method/-a-u-t-o-m-a-t-i-c/index.html) method will decide if a [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) method will be used (if smaller than or equal to threshold) or a [Method.SPEED](-method/-s-p-e-e-d/index.html) method will be used (if larger than threshold).



The bigger the image is being scaled to, the less noticeable degradations in the image becomes and the faster algorithms can be selected.



The value of this threshold (1600) was chosen after visual, by-hand, A/B testing between different types of images scaled with this library; both photographs and screenshots. It was determined that images below this size need to use a [Method.BALANCED](-method/-b-a-l-a-n-c-e-d/index.html) scale method to look decent in most all cases while using the faster [Method.SPEED](-method/-s-p-e-e-d/index.html) method for images bigger than this threshold showed no noticeable degradation over a BALANCED scale.




