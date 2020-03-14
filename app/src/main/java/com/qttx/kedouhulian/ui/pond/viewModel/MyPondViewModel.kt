package com.qttx.kedouhulian.ui.pond.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/4/12 0012
 */
class MyPondViewModel constructor(val api: Api) : BaseListViewModel<PondBean>() {


    val updateLiveData = NetLiveData<Any>()
    /**
     * 我的池塘列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondBean>>> {
        return api.myPool(params)
    }


    fun poolUpdateType(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.poolUpdateType(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(updateLiveData)
    }

}