package com.stay.toolslibrary.utils.extension

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.stay.toolslibrary.base.BaseApplication
import com.stay.toolslibrary.net.NetExceptionHandle
import com.stay.toolslibrary.net.NetMsgBeanProvider
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author huangyr
 * @date 2019/2/14 0014
 */

fun <T : NetMsgBeanProvider> bindScheduler(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T : NetMsgBeanProvider> Observable<T>.bindException(): Observable<T> {
    return flatMap {
        return@flatMap checkResult(it)
    }
}

fun <T : NetMsgBeanProvider> bindException(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.flatMap {
            return@flatMap checkResult(it)
        }
    }
}

fun <T : NetMsgBeanProvider> Observable<T>.bindSchedulerException(): Observable<T> {
    return bindScheduler().flatMap {
        return@flatMap checkResult(it)
    }
}


fun <T : NetMsgBeanProvider> bindCatch(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                return@flatMap checkResult(it)
            }
    }
}

fun <T : NetMsgBeanProvider> Observable<T>.bindSchedulerExceptionLife(
    owner: LifecycleOwner,
    stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP
): Observable<T> {
    return bindScheduler().bindLifecycle(owner, stopEvent).flatMap {
        return@flatMap checkResult(it)
    }
}


fun <T : NetMsgBeanProvider> Observable<T>.bindSchedulerLife(
        owner: LifecycleOwner,
        stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP
): Observable<T> {
    return bindScheduler().bindLifecycle(owner, stopEvent)
}



fun <T : NetMsgBeanProvider> bindSchedulerExceptionLife(
    owner: LifecycleOwner,
    stopEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP
): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .bindLifecycle(owner, stopEvent)
            .flatMap {
                return@flatMap checkResult(it)
            }
    }

}

private fun <T : NetMsgBeanProvider> checkResult(t: T?): ObservableSource<T> {
    if (t != null && BaseApplication.getApplication().requestConfig().successCode.contains(t.netCode)) {
        return Observable.just(t)
    }
    return Observable.create {
        if (t == null) {
            it.onError(NetExceptionHandle.ServerException(NetExceptionHandle.ERROR.UNKNOWN, ""))
        } else {
            it.onError(NetExceptionHandle.ServerException(t.netCode, t.netMsg))
        }
    }
}