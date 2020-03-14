package com.qttx.kedouhulian.bean

import android.os.Parcel
import android.os.Parcelable

data class GroupInfoBean(
        var group_avatar: String = "",
        var group_id: String = "",
        var group_no: String = "",
        var group_name: String = "",
        var is_grouper: Int = 0,
        var group_type: Int = 0,
        var is_receive: Any? = Any(),
        var list: MutableList<FriendBean> = mutableListOf()
)

