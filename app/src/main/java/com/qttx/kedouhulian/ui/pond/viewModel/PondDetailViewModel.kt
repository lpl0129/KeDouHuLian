package com.qttx.kedouhulian.ui.pond.viewModel


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

class PondDetailViewModel constructor(val api: Api) : BaseListViewModel<PondHistoryBean>() {


    val detailLiveData = NetLiveData<PondDetailBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)


    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondHistoryBean>>> {
        return api.changeLog(params)
    }

    override fun getListData(own: LifecycleOwner, isRefresh: Boolean, requestParams: Map<String, String>?) {
        if (isRefresh) {
            getHeaderView(own, requestParams!!.getValue("id"))
        }
        super.getListData(own, isRefresh, requestParams)
    }


    fun getHeaderView(owner: LifecycleOwner, id: String) {
        api.poolInfo(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(detailLiveData)
    }

}
