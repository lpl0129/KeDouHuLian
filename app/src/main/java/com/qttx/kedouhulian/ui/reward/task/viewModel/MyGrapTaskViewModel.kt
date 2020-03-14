package com.qttx.kedouhulian.ui.reward.task.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.MyGrapTaskBean
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.TaskBean
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/4/12 0012
 */
class MyGrapTaskViewModel constructor(val api: Api) : BaseListViewModel<MyGrapTaskBean>() {

    val examineLiveData = NetLiveData<Any>()

    var status: Int = 0
    var ordrderid: Int = 0

    /**
     * 我的抢单列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<MyGrapTaskBean>>> {
        return api.myOrderList(params)
    }


}