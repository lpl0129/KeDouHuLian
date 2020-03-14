package com.qttx.kedouhulian.bean

import android.os.Parcel
import android.os.Parcelable

data class PayBean(
        var data_id: String = "",
        var fromtype: Int = 0,
        var paymoney: Double = 0.0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(data_id)
        writeInt(fromtype)
        writeDouble(paymoney)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PayBean> = object : Parcelable.Creator<PayBean> {
            override fun createFromParcel(source: Parcel): PayBean = PayBean(source)
            override fun newArray(size: Int): Array<PayBean?> = arrayOfNulls(size)
        }
    }
}