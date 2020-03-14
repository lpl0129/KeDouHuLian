package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class SearchBean(
        var avatar: String = "",
        var is_friend: Int = 0,
        var mobile: String = "",
        var group_id: String = "",
        var group_no: String = "",
        var uid: Int = 0,
        var is_group: Int = 0,
        var type: Int = 0,
        var name: String = "",
        var username: String = ""

)