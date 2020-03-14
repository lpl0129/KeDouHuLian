package com.qttx.kedouhulian.bean

data class TaskMsgBean(
        var avatar: String = "",
        var content: String = "",
        var b_id: String = "",
        var b_nickname: String = "",
        var expand: Boolean = false,
        var create_time: Long = 0,
        var create_time_text: String = "",
        var id: Int = 0,
        var status: Int = 0,
        var task_id: Int = 0,
        var uid: Int = 0,
        var u_id: Int = 0,
        var username: String = "",
        var nickname: String = "",
        var arr: ArrayList<TaskMsgBean> = ArrayList(),
        var bounty_id: Int = 0
)