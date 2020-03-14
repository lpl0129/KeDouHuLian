package com.qttx.kedouhulian.bean

import android.os.Parcel
import android.os.Parcelable


data class Userinfo(
    val avatar: String = "",
    val createtime: Int = 0,
    val expires_in: Int = 0,
    val expiretime: Int = 0,
    val id: String = "",
    val mobile: String = "",
    val nickname: String = "",
    val rongyun_token: String = "",
    val score: String = "",
    val token: String = "",
    val user_id: String = "",
    val username: String = "",
    var whether_info: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(avatar)
        writeInt(createtime)
        writeInt(expires_in)
        writeInt(expiretime)
        writeString(id)
        writeString(mobile)
        writeString(nickname)
        writeString(rongyun_token)
        writeString(score)
        writeString(token)
        writeString(user_id)
        writeString(username)
        writeInt(whether_info)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Userinfo> = object : Parcelable.Creator<Userinfo> {
            override fun createFromParcel(source: Parcel): Userinfo = Userinfo(source)
            override fun newArray(size: Int): Array<Userinfo?> = arrayOfNulls(size)
        }
    }
}