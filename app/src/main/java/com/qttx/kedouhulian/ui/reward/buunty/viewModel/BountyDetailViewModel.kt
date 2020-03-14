package com.qttx.kedouhulian.ui.reward.buunty.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.utils.getUserLocation
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class BountyDetailViewModel constructor(val api: Api) : BaseListViewModel<TaskMsgBean>() {

    val deatiLiveData = NetLiveData<BountyBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val getTaskLiveData = NetLiveData<Any>()
    val delectLiveData = NetLiveData<RedPacketDeleteBean>()
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskMsgBean>>> {
        return api.bounty_reply_list(params)
    }

    override fun getListData(own: LifecycleOwner, isRefresh: Boolean, requestParams: Map<String, String>?) {
        if (isRefresh) {
            getHeaderView(own, requestParams!!.getValue("bounty_id"))
        }
        super.getListData(own, isRefresh, requestParams)
    }

    fun getHeaderView(owner: LifecycleOwner, id: String) {
        val location = getUserLocation()
        val map = mutableMapOf<String, String>()
        map["bounty_id"] = id
        location?.let {
            map["province"] = location.provinceId.toString()
            map["city"] = location.cityId.toString()
            map["district"] = location.districtId.toString()
        }
        api.get_bounty_info(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(deatiLiveData)
    }

    /**
     * 悬赏报名
     */
    fun getTask(own: LifecycleOwner, id: String) {

        val location = getUserLocation()
        val map = mutableMapOf<String, String>()
        map["bounty_id"] = id
        location?.let {
            map["province"] = location.provinceId.toString()
            map["city"] = location.cityId.toString()
            map["district"] = location.districtId.toString()
        }
        api.bounty_join(map)
                .bindSchedulerExceptionLife(own)
                .subscribe(getTaskLiveData)

    }

    fun delectTask(own: LifecycleOwner, taskid: String, status: String) {
        api.bounty_cancel(taskid, status)
                .bindSchedulerExceptionLife(own)
                .subscribe(delectLiveData)
    }
}