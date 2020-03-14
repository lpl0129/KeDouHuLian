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
class MyPublishBountyViewModel constructor(val api: Api) : BaseListViewModel<BountyBean>() {


    /**
     * 获取任务列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>> {
        return api.my_bounty_list(params)
    }

}