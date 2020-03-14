package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class TradeMarketBean(
        var id: Int = 0,
        var seller_num: String = "",
        var buy_num: String = "",
        var status: Int = 0,
        var uid: Int = 0,
        var unit: String = "",
        @SerializedName("nickname")
        var username: String = "",
        var avatar: String = "",

        var buyer_name: String = "",
        var seller_name: String = "",
        var amount: String = "",
        var create_time: String = "",
        var view: String = "",
        var ctime: String = "",
        var is_top: Int = 0,
        var is_alter:Int = 0

)