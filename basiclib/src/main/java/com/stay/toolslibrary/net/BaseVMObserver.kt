package com.stay.toolslibrary.net

import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.extension.converterError
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 */
abstract class BaseVMObserver<T : NetMsgBeanProvider>(var loadingStatus: ViewLoadingStatus,
                                                      var errorStatus: ViewErrorStatus,
                                                      var loadingText: String = "加载中...",
                                                      var listener: T.() -> Unit
) : Observer<T> {
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
        LogUtils.e("exception",e.message)
        val msgBean = e.converterError()
        msgBean.status = errorStatus.viewStatus
        postPoJo(msgBean, instance)
    }


    override fun onComplete() {

    }

    abstract fun getInstanceClass(): T

    private fun postPoJo(msgBean: NetMsgBean, t: T) {
        t.setNetMsgBean(msgBean)
        t.listener()
    }

}
