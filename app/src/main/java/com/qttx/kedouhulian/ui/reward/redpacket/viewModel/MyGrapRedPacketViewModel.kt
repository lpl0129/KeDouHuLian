package com.qttx.kedouhulian.ui.reward.redpacket.viewModel

import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.RedPacketBean
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/4/12 0012
 */
class MyGrapRedPacketViewModel constructor(val api: Api) : BaseListViewModel<RedPacketBean>() {

    /**
     * 获取任务列表
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketBean>>> {
        return api.my_get_platformred_list(params)
    }

}