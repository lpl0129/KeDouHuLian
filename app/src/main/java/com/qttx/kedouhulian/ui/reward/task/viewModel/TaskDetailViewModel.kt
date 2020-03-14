package com.qttx.kedouhulian.ui.reward.task.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import com.lxj.xpopup.widget.LoadingView
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.utils.getUserId
import com.qttx.kedouhulian.utils.getUserLocation
import com.stay.toolslibrary.library.swaipLayout.Utils
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class TaskDetailViewModel constructor(val api: Api) : BaseListViewModel<TaskMsgBean>() {

    val taskDeatiLiveData = NetLiveData<TaskDetailBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val getTaskLiveData = NetLiveData<Any>()
    val delectLiveData = NetLiveData<RedPacketDeleteBean>()
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskMsgBean>>> {
        return api.replyList(params)
    }

    override fun getListData(own: LifecycleOwner, isRefresh: Boolean, requestParams: Map<String, String>?) {
        if (isRefresh) {
            getHeaderView(own, requestParams!!.getValue("task_id"))
        }
        super.getListData(own, isRefresh, requestParams)
    }

    fun getHeaderView(owner: LifecycleOwner, id: String) {

        if(taskDeatiLiveData.value==null)
        {
            taskDeatiLiveData.loadingStatus=ViewLoadingStatus.LOADING_VIEW
        }else
        {
            taskDeatiLiveData.loadingStatus=ViewLoadingStatus.LOADING_DIALOG
        }

        api.taskDetail(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(taskDeatiLiveData)
    }

    fun getTask(own: LifecycleOwner, id: String) {
        val location = getUserLocation()

        val map = mutableMapOf<String, String>()
        map["task_id"] = id
        location?.let {
            map["province"] = location.provinceId.toString()
            map["city"] = location.cityId.toString()
            map["district"] = location.districtId.toString()
        }


        api.getTask(map)
                .bindSchedulerExceptionLife(own)
                .subscribe(getTaskLiveData)
    }

    fun delectTask(own: LifecycleOwner, taskid: String) {
        api.delectTask(taskid)
                .bindSchedulerExceptionLife(own)
                .subscribe(delectLiveData)

    }

    val taskOrderDeatiLiveData = NetLiveData<TaskOrderDetailBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    fun getTaskOrderDetail(owner: LifecycleOwner, id: String, orderid: String) {
        api.taskOrderDetail(id, orderid)
                .bindSchedulerExceptionLife(owner)
                .subscribe(taskOrderDeatiLiveData)
    }
}
