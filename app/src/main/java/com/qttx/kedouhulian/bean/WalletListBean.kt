package com.qttx.kedouhulian.bean

data class WalletListBean(
        var after: String = "",
        var after_frozen: String = "",
        var before: String = "",
        var before_frozen: String = "",
        var cate: Int = 0,
        var create_time_text: String = "",
        var createtime: Int = 0,
        var data_id: String = "",
        var from: Int = 0,
        var id: String = "",
        var memo: String = "",
        var money: String = "",
        var score: String = "",

        var type: Int = 0,
        var user_id: String = ""
)