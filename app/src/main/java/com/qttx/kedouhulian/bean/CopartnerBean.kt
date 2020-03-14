package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class CopartnerBean(
        var QRcode: String = "",
        var accumulated_earnings: String  = "",
        var city: Int = 0,
        var district: Int = 0,
        var invite_code: String = "",
        var jointime: String = "",
        var link: String = "",
        var partnercount: Int = 0,
        var province: Int = 0,
        @SerializedName("nickname")
        var username: String = "",
        var vpartnercount: Int = 0
)