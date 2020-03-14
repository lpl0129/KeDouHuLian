package com.qttx.kedouhulian.ui.trade.viewModel

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
class MyPublishTradeEndViewModel constructor(val api: Api) : BaseListViewModel<TradeMarketBean>() {

    /**
     * 我的发布列表-挂售中
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>> {
        return api.my_sellorder_list(params)
    }


}