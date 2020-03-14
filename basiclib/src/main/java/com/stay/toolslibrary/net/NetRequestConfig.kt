package com.stay.toolslibrary.net

import okhttp3.OkHttpClient

/**
 * @author huangyr
 * @date 2019/2/14 0014
 */
data class NetRequestConfig(val successCode: List<Int>,
                            var pageInitial: Int = 1,
                            var pageSizeInitial: Int = 15,
                            var pageKey: String = "page",
                            var pageSizeKey: String = "page_size",
                            var okHttpBuilder: OkHttpClient.Builder? = null
)
