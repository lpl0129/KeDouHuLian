package com.stay.toolslibrary.utils.extension

import android.support.annotation.DimenRes
import com.stay.toolslibrary.base.BaseApplication


fun Int.sp2px(): Int = BaseApplication.getApplication().sp2px(this)
fun Float.sp2px(): Int = BaseApplication.getApplication().sp2px(this)

fun Float.dp2px(): Int = BaseApplication.getApplication().dp2px(this)

fun Int.dp2px(): Int = BaseApplication.getApplication().dp2px(this)

fun Int.px2dp(): Float = BaseApplication.getApplication().px2dp(this)

fun Int.px2sp(): Float = BaseApplication.getApplication().px2sp(this)

fun @receiver:DimenRes Int.dimen2px(): Int = BaseApplication.getApplication().dimen2px(this)






