package com.qttx.kedouhulian.ui.reward.buunty.viewModel

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
class MyGrapBountyViewModel constructor(val api: Api) : BaseListViewModel<BountyBean>() {

    val confirmLiveData = NetLiveData<Any>()

    val applyFinishLiveData = NetLiveData<Any>()

    /**
     * 获取任务列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>> {
        return api.my_bounty_order_list(params)
    }

    fun applyFinish(own: LifecycleOwner, bounty_id: String) {
        api.apply_finish(bounty_id)
                .bindSchedulerExceptionLife(own)
                .subscribe(applyFinishLiveData)
    }

    fun applyConfirm(own: LifecycleOwner, bounty_id: String,isAgree:String) {
        api.bounty_fire_confirm(bounty_id,isAgree)
                .bindSchedulerExceptionLife(own)
                .subscribe(confirmLiveData)
    }
}