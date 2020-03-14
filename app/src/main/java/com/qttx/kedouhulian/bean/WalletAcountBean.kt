package com.qttx.kedouhulian.bean

import android.os.Parcel
import android.os.Parcelable

data class WalletAcountBean(
        var money: String = "",
        var w_id: String = "",
        var w_number: String = "",
        var w_realname: String = "",
        var y_bankname: String = "",
        var y_id: String = "",
        var y_number: String = "",
        var y_realname: String = "",
        var z_id: String = "",
        var z_number: String = "",
        var z_realname: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(money)
        writeString(w_id)
        writeString(w_number)
        writeString(w_realname)
        writeString(y_bankname)
        writeString(y_id)
        writeString(y_number)
        writeString(y_realname)
        writeString(z_id)
        writeString(z_number)
        writeString(z_realname)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WalletAcountBean> = object : Parcelable.Creator<WalletAcountBean> {
            override fun createFromParcel(source: Parcel): WalletAcountBean = WalletAcountBean(source)
            override fun newArray(size: Int): Array<WalletAcountBean?> = arrayOfNulls(size)
        }
    }
}