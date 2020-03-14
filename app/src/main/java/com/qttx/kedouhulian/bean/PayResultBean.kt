package com.qttx.kedouhulian.bean

data class PayResultBean(
        var alipay: String = "",
        val wechat: WeixinPayBean?
)