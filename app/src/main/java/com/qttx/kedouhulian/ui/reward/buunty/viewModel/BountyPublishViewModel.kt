package com.qttx.kedouhulian.ui.reward.buunty.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class BountyPublishViewModel constructor(val api: Api) : BaseViewModel() {

    //0是显示,1是隐藏
    var isImageShow = 0

    var isSexLimit = 0

    var isTop = 0
    var limitsize = 9

    val configLiveData = NetLiveData<HunterConfig>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val tokenLiveData = NetLiveData<String>()

    val saveLiveData = NetLiveData<PayBean>()

    fun getData(owner: LifecycleOwner) {
        api.bounty_get_setting()
                .bindSchedulerExceptionLife(owner)
                .subscribe(configLiveData)
    }

    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }

    fun saveData(owner: LifecycleOwner,map:Map<String,String>) {
        api.bounty_add(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(saveLiveData)
    }
}
