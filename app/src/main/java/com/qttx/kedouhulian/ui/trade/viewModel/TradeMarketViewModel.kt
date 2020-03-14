package com.qttx.kedouhulian.ui.trade.viewModel

import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.TradeMarketBean
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/5/8 0008
 */

class TradeMarketViewModel constructor(val api: Api) : BaseListViewModel<TradeMarketBean>() {


    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>> {
        return api.trade_sell_list(params)
    }

}
