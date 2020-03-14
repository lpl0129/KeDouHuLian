package com.qttx.kedouhulian.ui.reward.redpacket.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.RedPacketBean
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
class MyPublishRedPacketViewModel constructor(val api: Api) : BaseListViewModel<RedPacketBean>() {


    /**
     * 获取任务列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketBean>>> {
        return api.my_platformred_list(params)
    }

}