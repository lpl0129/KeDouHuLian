package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class RedPacketBean(
        var avatar: String = "",
        var content: String = "",
        var create_time: String = "",
        var getmoney: Double = 0.0,
        var id: Int = 0,
        var imgs: String? = null,
        var is_top: Int = 0,
        var isget: Int = 0,
        var limit_age: String = "",
        var limit_area: Int = 0,
        var limit_num: Int = 0,
        var limit_sex: Int = 0,
        var link: String = "",
        var red_pass: String = "",
        var red_type: String = "",
        var sycount: Int = 0,
        var sy_price: String = "",
        var ylcount: Int = 0,
        var create_time_text: String = "",
        var title: String = "",
        var total_num: Int = 0,
        var total_price: String = "",
        var uid: String = "",
        @SerializedName("nickname")
        var username: String = "",
        var video_length: String? = null,
        var video_url: String? = null,
        var waite_time: Int = 0,
        var status: String = "",
        var score: String = "",
        var month: String = "",
        var day: String = "",
        var red_id: String = "",
        var red_price: String = "",
        var current_status: String = ""

)