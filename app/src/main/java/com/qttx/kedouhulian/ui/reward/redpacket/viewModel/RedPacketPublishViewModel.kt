package com.qttx.kedouhulian.ui.reward.redpacket.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.bean.SystemConfig
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class RedPacketPublishViewModel constructor(val api: Api) : BaseViewModel() {


    var onceType:Int=0

    var isSexLimit = 0

    var isTop = 0

    var ageLimit = mutableListOf(0)

    var redPacketModel:Int=1


    val tokenLiveData = NetLiveData<String>()

    val appConfig = NetLiveData<SystemConfig>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    val saveLiveData = NetLiveData<PayBean>()
    /**
     * 获取token
     */
    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }

    /**
     * 获取app配置信息
     */
    fun getAppConfig(owner: LifecycleOwner) {
        api.platformred_get_setting()
                .bindSchedulerExceptionLife(owner)
                .subscribe(appConfig)
    }

    fun saveData(owner: LifecycleOwner,map:Map<String,String>) {
        api.platformRedAdd(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(saveLiveData)
    }


}
