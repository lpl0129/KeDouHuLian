package com.stay.toolslibrary.net

import com.stay.basiclib.R


/**
 * Created by huang on 2017/10/26.
 */

data class NetMsgBean(

        var status: ViewStatus = ViewStatus.SUCCESS,

        var errorCode: Int = 0,

        var errorMsg: String = "网络连接错误,请稍后再试",

        var loadingMsg: String = "加载中...",
        /**
         * 显示的错误图片
         */
        var errorImageRes: Int = R.drawable.toolslib_no_network,
        /**
         * 是否显示重新加载按钮
         */
        var isCanRetry: Boolean = true,
        /**
         * 是否是服务器错误
         */
        var isServiceError: Boolean = false,
        /**
         * 是否需要特殊处理
         */
        var isSpecial: Boolean = false

)
