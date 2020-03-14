package com.qttx.kedouhulian.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "city")
data class RegionsBean(

        @PrimaryKey var id: Int = 0,
        var adcode: String = "",
        var center: String = "",
        var citycode: String = "",
        var lat: String = "",
        var level: Int = 0,
        var level_name: String = "",
        var lng: String = "",
        var mergename: String = "",
        var name: String = "",
        var pid: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readInt(),
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(adcode)
        writeString(center)
        writeString(citycode)
        writeString(lat)
        writeInt(level)
        writeString(level_name)
        writeString(lng)
        writeString(mergename)
        writeString(name)
        writeInt(pid)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RegionsBean> = object : Parcelable.Creator<RegionsBean> {
            override fun createFromParcel(source: Parcel): RegionsBean = RegionsBean(source)
            override fun newArray(size: Int): Array<RegionsBean?> = arrayOfNulls(size)
        }
    }
}
