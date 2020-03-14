package com.stay.toolslibrary.base

import android.app.Application
import android.content.Context
import com.hjq.toast.ToastUtils
import com.stay.basiclib.R
import com.stay.toolslibrary.di.remoteModule
import com.stay.toolslibrary.net.NetExceptionHandle
import com.stay.toolslibrary.net.NetManager
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.NetRequestConfig
import com.stay.toolslibrary.utils.AppUtils
import com.stay.toolslibrary.utils.PathUtils
import com.stay.toolslibrary.utils.PinyinUtils
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module

/**
 * @author huangyr
 * @date 2019/1/21 0021
 */
abstract class BaseApplication : Application() {
    public lateinit var  requestConfig:NetRequestConfig

    companion object {
        private var instance: BaseApplication? = null
        fun getApplicationContext(): Context {
            return instance!!
        }

        fun getApplication(): BaseApplication {
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        requestConfig = requestConfig()
        requestConfig.okHttpBuilder?.let {
            NetManager.registerHttpBuilder(it)
        }
        AppUtils.init(this)
        ToastUtils.init(this)
        PathUtils.initPath(PinyinUtils.ccs2Pinyin(resources.getString(R.string.app_name)))
        startAppKoin()
    }

    private fun startAppKoin()
    {

        val mutableList= mutableListOf<Module>()
        mutableList.add(remoteModule)
        mutableList.addAll(configInjectModule())
        startKoin(this, mutableList)
    }
    /**
     * 将异常处理为NetMsgBean
     */
    fun converterError(e: Throwable): NetMsgBean {
        var isfromServer = e is NetExceptionHandle.ServerException
        val exception = NetExceptionHandle.handleException(e)
        val netMsgBean = NetMsgBean(
                errorCode = exception.code,
                errorMsg = exception.errormsg,
                isServiceError = isfromServer)
        return handleNetException(netMsgBean)
    }

    /**
     * 处理netMsgBean
     * 设置特殊code,
     */
    abstract fun handleNetException(netMsgBean: NetMsgBean): NetMsgBean

    /**
     * 网络请求配置
     */
    abstract fun requestConfig(): NetRequestConfig
    /**
     * 网络请求配置
     */
    abstract fun configInjectModule(): List<Module>
}
