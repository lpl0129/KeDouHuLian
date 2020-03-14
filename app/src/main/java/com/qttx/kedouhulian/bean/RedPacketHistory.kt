package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

/**
 * @author huangyr
 * @date 2019/4/23 0023
 */
data class RedPacketHistory
(
        var id: String = "",
        @SerializedName("nickname")
        var username: String = "",
        var avatar: String = "",
        var red_price: String = "",
        var create_time: String = ""
)