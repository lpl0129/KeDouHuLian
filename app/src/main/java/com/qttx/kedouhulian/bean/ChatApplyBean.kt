package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class ChatApplyBean(
        var avatar: String = "",
        var content: String = "",
        var mid: String = "",
        var fmid: String = "",
        var uid: String = "",
        var status: Int = 0,
        @SerializedName("nickname")
        var username: String = ""
)