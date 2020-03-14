package com.qttx.kedouhulian.ui.reward.task.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.bean.TaskConfig
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import java.util.ArrayList

class TaskPublishViewModel constructor(val api: Api) : BaseViewModel() {

    var isImageShow = 1

    var isSexLimit = 0


    var isTop = 0
    var limitsize = 6

    val configLiveData = NetLiveData<TaskConfig>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val tokenLiveData = NetLiveData<String>()

    val saveLiveData = NetLiveData<PayBean>()

    fun getData(owner: LifecycleOwner) {
        api.taskTypeList()
                .bindSchedulerExceptionLife(owner)
                .subscribe(configLiveData)
    }

    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }

    fun saveData(owner: LifecycleOwner,map:Map<String,String>) {
        api.publishTask(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(saveLiveData)
    }
}
