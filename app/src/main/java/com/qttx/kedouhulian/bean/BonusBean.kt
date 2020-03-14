package com.qttx.kedouhulian.bean

data class BonusBean(
        var end_strtotime: Long = 0,
        var endtime: String = "",
        var money: String = "",
        var fh_money: Double =0.00,
        var start_strtotime:Long = 0,
        var starttime: String = ""
)