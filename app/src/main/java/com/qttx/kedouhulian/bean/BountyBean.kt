package com.qttx.kedouhulian.bean

data class BountyBean(
        var aid: Int = 0,
        var area_id: Int = 0,
        var area_name: String = "",
        var area_num: String = "",
        var avatar: String = "",
        var cid: Int = 0,
        var content: String = "",
        var create_time: Long = 0,
        var employ_num: Int = 0,
        var fee: String = "",
        var finish_num: Int = 0,
        var group_id: String = "",
        var hidden_img: Int = 0,
        var id:  String = "",
        var imgs: String = "",
        var imgs_list: List<String> = listOf(),
        var is_top: Int = 0,
        var limit_area: Int = 0,
        var limit_sex: Int = 0,
        var nickname: String = "",
        var paymoney: String = "",
        var price: String = "",
        var service_price: Double = 0.0,
        var status: Int = 0,
        var submit_num: Int = 0,
        var title: String = "",
        var top_price: String = "",
        var total_num: Int = 0,
        var type_name: String = "",
        var bounty_order_id: String = "",
        var uid: String = "",
        var is_puter: Int = 0,
        var seller_score: String="",
        var order_status: Int = 0,
        var agree: Int = 0,
        var is_apply_finish: Int = 0,

        var unit: String = "",
        var current_status: String = ""

)