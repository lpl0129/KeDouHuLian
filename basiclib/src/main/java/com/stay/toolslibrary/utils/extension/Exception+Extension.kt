package com.stay.toolslibrary.utils.extension

import com.stay.toolslibrary.base.BaseApplication
import com.stay.toolslibrary.net.NetMsgBean
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by Tony Shen on 2017/6/30.
 */

fun Throwable.getStackTraceText(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    printStackTrace(pw)
    return sw.toString()
}

 fun Throwable.converterError(): NetMsgBean {
    return BaseApplication.getApplication().converterError(this)
}

