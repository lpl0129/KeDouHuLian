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
class MyPublishTradeIngViewModel constructor(val api: Api) : BaseListViewModel<TradeMarketBean>() {

    val confirmLiveData = NetLiveData<Any>()

    val applyFinishLiveData = NetLiveData<Any>()

    /**
     * 我的发布列表-挂售中
     */
    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>> {
        return api.my_sell_list(params)
    }

    fun applyCancel(own: LifecycleOwner, bounty_id: String) {
        api.sell_cancel(bounty_id)
                .bindSchedulerExceptionLife(own)
                .subscribe(applyFinishLiveData)
    }

    fun update_price(own: LifecycleOwner, id: String,price:String) {
        api.update_price(id,price)
                .bindSchedulerExceptionLife(own)
                .subscribe(confirmLiveData)
    }
}