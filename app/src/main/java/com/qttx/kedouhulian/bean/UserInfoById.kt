package com.qttx.kedouhulian.bean

import com.qttx.kedouhulian.utils.RongGenerate

class UserInfoById {

    var avatar: String = ""
    var id: String = ""
    var is_friend: Int = 0
    var gender: Int = 0
    var score: String = ""

    var nickname: String = ""

    var remark: String = ""
    var username: String = ""
    val birthday:String=""
    val area_name:String=""
    val birthday_name:String=""
    val message_num: Int = 0

    var mark: String = ""
        get() = if (remark.isEmpty()) nickname else remark


    var avatarUrl: String = ""
        get() = if (avatar.isEmpty()) RongGenerate.generateDefaultAvatar(mark, id) else avatar


}
