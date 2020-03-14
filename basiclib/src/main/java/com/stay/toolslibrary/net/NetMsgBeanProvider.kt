package com.stay.toolslibrary.net

/**
 * @author huangyr
 * @date 2019/1/17 0017
 */
interface NetMsgBeanProvider {

    fun getNetMsgBean(): NetMsgBean

    fun setNetMsgBean(bean: NetMsgBean)

    var netCode:Int
    var netMsg:String?

}

