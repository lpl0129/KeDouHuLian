package com.qttx.kedouhulian.bean

/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
data class WaitVerifyTaskBean(
        var amend_status: Int = 0,
        var nickname: String = "",
        var avatar: String = "",
        var imgs_list: List<String> = listOf(),
        var create_time_text: String = ""
)