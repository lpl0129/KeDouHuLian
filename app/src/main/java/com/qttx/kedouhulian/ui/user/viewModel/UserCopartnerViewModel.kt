package com.qttx.kedouhulian.ui.user.viewModel


import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class UserCopartnerViewModel constructor(val api: Api) : BaseListViewModel<CopartnerBean>() {

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<CopartnerBean>>> {

        return api.partnerList(params)
    }

    val copartenerNetLiveData = NetLiveData<CopartnerBean>()

    fun getInfo(own: LifecycleOwner) {
        api.partner()
                .bindSchedulerExceptionLife(own)
                .subscribe(copartenerNetLiveData)
    }
}
