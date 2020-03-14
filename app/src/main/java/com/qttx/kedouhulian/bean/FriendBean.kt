package com.qttx.kedouhulian.bean

import com.qttx.kedouhulian.utils.RongGenerate

class FriendBean {
    var avatar: String = ""

    var remark: String = ""

    var is_receive: Int = 0
    var uid: String = ""
    var status: Int = 0
    var fmid: String = ""
    var content: String = ""
    var nickname: String = ""

    var pyname: String = ""

    var select:Boolean=false

    var canSelect:Boolean=true

    var is_grouper:Int=0

    var mark: String = ""
        get() = if (remark.isEmpty()) nickname else remark

    var avatarUrl: String = ""
        get() = if (avatar.isEmpty()) RongGenerate.generateDefaultAvatar(mark, uid) else avatar

}