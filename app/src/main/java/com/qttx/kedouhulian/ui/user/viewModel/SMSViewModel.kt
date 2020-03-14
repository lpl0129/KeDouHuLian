package com.qttx.kedouhulian.ui.user.viewModel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.SmsBean
import com.qttx.kedouhulian.bean.SmsSendStatus
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.net.NetExceptionHandle
import com.stay.toolslibrary.net.SingleLiveEvent
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.extension.bindException
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.showToast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.statistics.UserData.phone
import java.util.concurrent.TimeUnit

/**
 * 短信相关
 * @author huangyr
 * @date 2019/4/2 0002
 */
class SMSViewModel constructor(val api: Api) : BaseViewModel() {


    var timeLiveData = SingleLiveEvent<SmsBean>()

    var smsLiveData = SingleLiveEvent<NetResultBean<Any>>()

    var checkPhoneLiveData = SingleLiveEvent<Boolean>()
    /**
     * @param phone
     * @param exist true 需要phone在数据库里存在,false需要phone在数据库里不存在,如果为null.则忽略存不存在都发
     */
    fun sendMsg(own: LifecycleOwner, phone: String, type: String, needExist: Boolean? = false) {
        if (TextUtils.isEmpty(phone)) {
            "请填写手机号".showToast()
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            "手机号格式不正确".showToast()
            return
        }
        api
            .check_mobile_exist(phone)
            .subscribeOn(Schedulers.io())
            .bindException()
            .flatMap {
                val isregNumberExist = it.data?.is_exist == 1
                checkPhoneLiveData.postValue(isregNumberExist)
                if (needExist != null) {
                    var errorMsg = if (!isregNumberExist && !needExist) {
                        //如果手机号不存在并且需要的就是不存在
                        ""
                    } else if (!isregNumberExist && needExist) {
                        //手机号不存在,但是需要是存在
                        "手机号未注册"
                    } else if (isregNumberExist && needExist) {
                        //手机号存在,并且需要就是存在
                        ""
                    } else {
                        //手机号存在,但是需要是不存在
                        "手机号已注册"
                    }
                    if (errorMsg.isEmpty()) {
                        return@flatMap api.smsSend(phone, type)
                    } else {
                        return@flatMap Observable.error<NetResultBean<Any>>(
                            NetExceptionHandle.ServerException(
                                1,
                                errorMsg
                            )
                        )
                    }
                } else {
                    return@flatMap api.smsSend(phone, type)
                }

            }.observeOn(AndroidSchedulers.mainThread())
            .bindSchedulerExceptionLife(own).subscribe(
                object : NetObserver<Any>(ViewLoadingStatus.LOADING_DIALOG,
                    block = {
                        smsLiveData.value = this
                    }
                ) {
                    override fun onNext(t: NetResultBean<Any>) {
                        super.onNext(t)
                        beginTime(own)
                    }
                }
            )
    }

    private fun beginTime(own: LifecycleOwner) {
        val count = 60
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map {
                return@map count - it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .bindLifecycle(own, Lifecycle.Event.ON_DESTROY)
            .doOnSubscribe {
                "验证码发送成功".showToast()
                val status = SmsBean(status = SmsSendStatus.onSendCodeBegin)
                timeLiveData.value = status
                //                mView.onSendCodeBegin()
            }.subscribe(object : Observer<Long> {
                override fun onNext(t: Long) {
                    val status = SmsBean(status = SmsSendStatus.onTimeCountChange, time = t)
                    timeLiveData.value = status
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    val status = SmsBean(status = SmsSendStatus.onSendFailed)
                    timeLiveData.value = status
                }

                override fun onComplete() {
                    val status = SmsBean(status = SmsSendStatus.onTimeComplete)
                    timeLiveData.value = status
                }
            })
    }

}