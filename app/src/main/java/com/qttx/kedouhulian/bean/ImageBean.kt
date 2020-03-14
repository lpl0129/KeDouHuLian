package com.qttx.kedouhulian.bean

import android.os.Parcel
import android.os.Parcelable

class ImageBean() : Parcelable {
    /**
     * 本地图片
     */
    var local: String? = null
    /**
     * 显示的图片
     */
    var showImage: String? = null
    /**
     * 是否是添加按钮
     */
    var isAdd: Boolean = false

    /**
     * 是否来自服务器图片
     */
    var isFromService: Boolean = false

    /**
     * 服务器图片
     */
    var img: String? = null

    constructor(parcel: Parcel) : this() {
        local = parcel.readString()
        showImage = parcel.readString()
        isAdd = parcel.readByte() != 0.toByte()
        isFromService = parcel.readByte() != 0.toByte()
        img = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(local)
        parcel.writeString(showImage)
        parcel.writeByte(if (isAdd) 1 else 0)
        parcel.writeByte(if (isFromService) 1 else 0)
        parcel.writeString(img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageBean> {
        override fun createFromParcel(parcel: Parcel): ImageBean {
            return ImageBean(parcel)
        }

        override fun newArray(size: Int): Array<ImageBean?> {
            return arrayOfNulls(size)
        }
    }

}