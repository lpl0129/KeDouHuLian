package com.qttx.kedouhulian.bean

import com.google.gson.annotations.SerializedName

data class TaskDetailBean(
        val finish_status: Int = 0,
        var avatar: String = "",
        var cid: Int = 0,
        var content: String = "",
        var create_time: Long = 0,
        var create_time_text: String = "",
        var dsnum: Int = 0,
        var finish_num: Int = 0,
        var heat: Int = 0,
        var id: Int = 0,
        var imgs: String = "",
        var imgs_list: List<String> = listOf(),
        var is_top: Int = 0,
        var limit_area: Int = 0,
        var limit_reply: Int = 0,
        var limit_sex: Int = 0,
        var paymoney: String = "",
        var rate: Double = 0.0,
        var score: String =  "",
        var service_price: String = "",
        var service_rate: String = "",
        var show_img: Int = 0,
        var sign: Int = 0,
        var status: Int = 0,
        var task_price: String = "",
        var task_time: Int = 0,
        var task_typename: String = "",
        var title: String = "",
        var top_price: String = "",
        var total_num: Int = 0,
        var uid: Int = 0,
        @SerializedName("nickname")
        var username: String = "",
        val limit_time:Long=0,
        var orderid:Long=0,
        val restrict_times:Int=0
)