package com.qttx.kedouhulian.bean

data class SystemConfig(
        var group_fee: String = "",
        var kd_card: String = "",
        var platfrom_pool_ratio: String = "",
        var pool_bebuyrate: String = "",
        var pool_deal: String = "",
        var pool_rate: String = "",
        var red_fee: Double = 0.0,
        var red_kd_ratio: String = "",
        var red_parent_ratio: String = "",
        var red_top:Double = 0.0,
        var task_kd_ratio: String = "",
        var task_partner_ratio: String = "",
        var task_top: String = "",
        var task_yj_ratio: String = "",
        var taskservice_fee: String = "",
        var trade_end_time: String = "",
        var trade_start_time: String = "",
        var trade_top: String = ""
)