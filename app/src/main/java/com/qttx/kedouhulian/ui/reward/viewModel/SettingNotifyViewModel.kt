package com.qttx.kedouhulian.ui.reward.viewModel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.qttx.kedouhulian.bean.SettingNotifyBean
import com.qttx.kedouhulian.bean.UserInfoById
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.utils.LocationHelper
import com.qttx.kedouhulian.utils.Utils
import com.qttx.kedouhulian.utils.saveUserLocation
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class SettingNotifyViewModel constructor(val api: Api) : BaseViewModel() {


    val msgLiveData = NetLiveData<SettingNotifyBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    fun getMsg(owner: LifecycleOwner,loadingStatus: ViewLoadingStatus=ViewLoadingStatus.LOADING_VIEW,errorStatus: ViewErrorStatus=ViewErrorStatus.ERROR_VIEW) {
        msgLiveData.loadingStatus=loadingStatus
        msgLiveData.errorStatus=errorStatus
        api.getMsgSwitch()
                .bindSchedulerExceptionLife(owner)
                .subscribe(msgLiveData)
    }

    val setmsgLiveData = NetLiveData<Any>()
    fun setMsg(owner: LifecycleOwner, key: String, value: String) {
        val map= mutableMapOf<String,String>()
        map[key]=value
        api.setMsgSwitch(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(setmsgLiveData)
    }
    val userinfo2LiveData = NetLiveData<UserInfoById>()

    fun getUserInfo(own: LifecycleOwner) {
        api.getUserInfo()
                .bindSchedulerExceptionLife(own)
                .subscribe(userinfo2LiveData)
    }
}