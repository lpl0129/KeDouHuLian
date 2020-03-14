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
class MyBuyTradeViewModel constructor(val api: Api) : BaseListViewModel<TradeMyBuy>() {

    /**
     * 我的发布列表-挂售中
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMyBuy>>> {
        return api.trade_my_buy_list(params)
    }

    val applyFinishLiveData = NetLiveData<Any>()
    fun applyCancel(own: LifecycleOwner, bounty_id: String) {
        api.trade_order_cancel(bounty_id)
                .bindSchedulerExceptionLife(own)
                .subscribe(applyFinishLiveData)
    }

}