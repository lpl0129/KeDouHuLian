package com.stay.toolslibrary.utils.extension

import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.stay.basiclib.R

/**
 * Created by huangyr
 * on 2017/11/22.
 * Glide图片加载工具类
 */

/**
 * @param view
 * @param url
 * @param defalutimage
 */
@JvmOverloads
fun ImageView.loadImage(url: String, defalutimage: Int = R.drawable.default_image_360_360) {
    val options = RequestOptions.placeholderOf(defalutimage)
    loadImage(url, options.error(defalutimage).centerCrop())
}

@JvmOverloads
fun ImageView.loadCircleAvatar(url: String, defalutimage: Int = R.drawable.user_avatar_default) {
    val options = RequestOptions.placeholderOf(defalutimage)
    loadImage(url, options.error(defalutimage).circleCrop())
}

@JvmOverloads
fun ImageView.loadCircleImage(url: String, defalutimage: Int = R.drawable.default_image_360_360) {
    val options = RequestOptions.placeholderOf(defalutimage)
    loadImage(url, options.error(defalutimage).circleCrop())
}

/**
 * 强制加载centerCrop圆角图片
 * @param view
 * @param url
 */

@JvmOverloads
fun ImageView.loadRountCenterImage(url: String, defalutimage: Int = R.drawable.default_image_360_360, mRadius: Int = 5) {
    val options = RequestOptions.placeholderOf(defalutimage)
    loadImage(url, options.error(defalutimage).transforms(CenterCrop(), RoundedCorners(mRadius.dp2px())))
}


@JvmOverloads
fun ImageView.loadRounImage(url: String, defalutimage: Int = R.drawable.default_image_360_360, mRadius: Int = 5) {
    val options = RequestOptions.placeholderOf(defalutimage)
    loadImage(url, options.error(defalutimage).transform(RoundedCorners(mRadius.dp2px())))
}
/**
 * 正常加载centerCrop圆角图片
 * 会根据ImageView的scaleType变化
 * 不设置||centerInside==》原图比例的圆角图片
 * fitXY==>满view圆角图片
 * centerCrop==>失去圆角属性
 * @param view
 * @param url
 */
/**
 * @param view
 * @param url
 * @param options
 */
fun ImageView.loadImage(url: String, options: RequestOptions) {
    val context = this.context
    Glide.with(context).load(url).apply(options).into(this)
}

fun ImageView.getSvaePath(url: String): String {
    return Glide.with(this)
            .downloadOnly()
            .load(url)
            .submit()
            .get()
            .absolutePath
}




