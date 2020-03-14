package com.qttx.kedouhulian.ui.reward.task.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class TaskSubmitViewModel constructor(val api: Api) : BaseViewModel() {
    val tokenLiveData = NetLiveData<String>()
    val submitLiveData = NetLiveData<Any>()
    var limitsize = 9

    fun saveData(owner: LifecycleOwner,map: Map<String,String>) {
        api.submitTask(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(submitLiveData)
    }
    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }
}
