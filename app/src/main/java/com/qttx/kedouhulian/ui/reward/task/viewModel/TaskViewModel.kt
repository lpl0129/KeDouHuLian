package com.qttx.kedouhulian.ui.reward.task.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/4/12 0012
 */
class TaskViewModel constructor(val api: Api) : BaseListViewModel<TaskBean>() {
    val configLiveData = NetLiveData<TaskConfig>(ViewLoadingStatus.LOADING_NO)
    /**
     * 获取任务列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskBean>>> {
        return api.taskList(params)
    }

    val typeList= mutableListOf<TaskConfigItem>()

    fun getData(owner: LifecycleOwner) {
        api.taskTypeList()
                .bindSchedulerExceptionLife(owner)
                .subscribe(configLiveData)
    }

}