package com.stay.toolslibrary.net

import android.util.Log
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.extension.converterError
import io.reactivex.disposables.Disposable


/**
 * 所有,网络请求bean,必须实现NetMsgBeanProvider
 * .
 */
abstract class BaseNetLiveData<T : NetMsgBeanProvider>(var loadingStatus: ViewLoadingStatus,
                                                       var errorStatus: ViewErrorStatus, var loadingText: String = "加载中...")
    : SingleLiveEvent<T>(), io.reactivex.Observer<T> {

    /**
     * 默认传输实例
     */
    var instance: T = getInstanceClass()
    /**
     * 传输PoJO
     */
    private var msgBean: NetMsgBean = NetMsgBean()


    override fun onSubscribe(d: Disposable) {
        msgBean.status = loadingStatus.viewStatus
        msgBean.loadingMsg = loadingText
        postPoJo(msgBean, instance)
    }


    override fun onNext(t: T) {
        msgBean.status = ViewStatus.SUCCESS
        postPoJo(msgBean, t)
    }

    override fun onError(e: Throwable) {
        LogUtils.e("exceotion",e.cause)
        val msgBean = e.converterError()
        msgBean.status = errorStatus.viewStatus
        postPoJo(msgBean, instance)
    }


    override fun onComplete() {

    }

    abstract fun getInstanceClass(): T

    private fun postPoJo(msgBean: NetMsgBean, t: T) {
        t.setNetMsgBean(msgBean)
        value = t
    }
}
