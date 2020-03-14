package com.qttx.kedouhulian.ui.reward.redpacket.viewModel


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

class RedPacketDetailViewModel constructor(val api: Api) : BaseListViewModel<RedPacketHistory>() {

    /**
     * 是否进行过倒计时
     */
    var hasCount=false

    val detailLiveData = NetLiveData<RedPacketBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val getTaskLiveData = NetLiveData<RedPacketResultBean>()

    val delectLiveData = NetLiveData<RedPacketDeleteBean>()
    val version = NetLiveData<Any>()
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketHistory>>> {
        return api.isGetList(params)
    }

    override fun getListData(own: LifecycleOwner, isRefresh: Boolean, requestParams: Map<String, String>?) {
        if (isRefresh) {
            getHeaderView(own, requestParams!!.getValue("id"))
        }
        super.getListData(own, isRefresh, requestParams)
    }


    fun getHeaderView(owner: LifecycleOwner, id: String) {
        val is_clear = if (detailLiveData.value == null) {
            "1"
        } else {
            "0"
        }
        api.platformredInfo(id, is_clear)
                .bindSchedulerExceptionLife(owner)
                .subscribe(detailLiveData)
    }

    fun getRedpacket(own: LifecycleOwner, id: String, word: String) {
        val location = getUserLocation()
        val map = mutableMapOf<String, String>()
        map["red_id"] = id
        map["red_pass"] = word
        location?.let {
            map["province"] = location.provinceId.toString()
            map["city"] = location.cityId.toString()
            map["district"] = location.districtId.toString()
        }
        api.getPlatformred(map)
                .bindSchedulerExceptionLife(own)
                .subscribe(getTaskLiveData)
    }

    fun delectTask(own: LifecycleOwner, taskid: String, status: String) {
        api.platformred_cancel(taskid, status)
                .bindSchedulerExceptionLife(own)
                .subscribe(delectLiveData)

    }
}
