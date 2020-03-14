package com.stay.toolslibrary.di

import com.stay.toolslibrary.net.NetManager
import org.koin.dsl.module.module

/**
 * @author huangyr
 * @date 2019/1/3 0003
 */


val remoteModule = module {
    single { NetManager.getClient() }
    /**
     * 注意此处注入的baseUrl.是无法发起网络请求的，
     * 在Baselib中的网络请求 需要使用@Url
     *使用从外部注入的url
     */
    single(name = "basiclib") { NetManager.getRetrofit("baseUrl", get()) }
}

