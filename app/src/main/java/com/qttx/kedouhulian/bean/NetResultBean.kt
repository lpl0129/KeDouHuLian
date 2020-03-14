package com.qttx.kedouhulian.bean

import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.NetMsgBeanProvider


/**
 * Created by huang on 2017/2/14.
 */

open class NetResultBean<T> : NetMsgBeanProvider {

    override var netCode: Int
        get() = code
        set(value) {}
    override var netMsg: String?
        get() = msg
        set(value) {}

    override fun setNetMsgBean(bean: NetMsgBean) {
        msgBean=bean
    }

    override fun getNetMsgBean(): NetMsgBean {
        return msgBean
    }

    var data: T? = null

    var msg: String? = null
    var msgBean: NetMsgBean = NetMsgBean()
    var code: Int = 0

}
