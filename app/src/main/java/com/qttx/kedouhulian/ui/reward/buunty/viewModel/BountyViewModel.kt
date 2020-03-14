package com.qttx.kedouhulian.ui.reward.buunty.viewModel

import com.qttx.kedouhulian.bean.BountyBean
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/4/12 0012
 */
class BountyViewModel constructor(val api: Api) : BaseListViewModel<BountyBean>() {

    /**
     * 获取红包
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>> {
        return api.get_bounty_list(params)
    }

}