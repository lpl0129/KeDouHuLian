package com.qttx.kedouhulian.bean

data class TaskOrderDetailBean(
        var orderinfo: TaskOrderOrderInfo,
        var taskinfo: TaskOrderTaskInfo
)

data class TaskOrderTaskInfo(
        var avatar: String = "",
        var cid: String = "",
        var content: String = "",
        var create_time: Long = 0,
        var create_time_text: String = "",
        var finish_num: Int = 0,
        var heat: Int = 0,
        var id: String = "",
        var imgs: String = "",
        var imgs_list: List<String> = listOf(),
        var is_top: Int = 0,
        var limit_area: Int = 0,
        var limit_reply: Int = 0,
        var limit_sex: Int = 0,
        var nickname: String = "",
        var paymoney: String = "",
        var receiver_num: Int = 0,
        var restrict_times: Int = 0,
        var score: String = "",
        var service_price: String = "",
        var service_rate: String = "",
        var show_img: Int = 0,
        var status: Int = 0,
        var task_price: String = "",
        var task_time: Int = 0,
        var task_typename: String = "",
        var title: String = "",
        var top_price: String = "",
        var total_num: Int = 0,
        var uid: String = ""
)

data class TaskOrderOrderInfo(
        var CDN_URL: String = "",
        var amend_status: Int = 0,
        var appeal: String = "",
        var city: Int = 0,
        var content: String = "",
        var create_time: Long = 0,
        var create_time_text: String = "",
        var district: Int = 0,
        var false_info: String = "",
        var finish_time: Long = 0,
        var finish_time_text: String = "",
        var from_uid: String = "",
        var id:  String = "",
        var imgs: String = "",
        var imgs_list: ArrayList<String> = arrayListOf(),
        var limit_time: Long = 0,
        var limit_time_text: String = "",
        var price: String = "",
        var province: Int = 0,
        var restrict_times: Int = 0,
        var score: String = "",
        var status: Int = 0,


        var task_id: String = "",
        var task_price: String = "",
        var uid: String = ""
)