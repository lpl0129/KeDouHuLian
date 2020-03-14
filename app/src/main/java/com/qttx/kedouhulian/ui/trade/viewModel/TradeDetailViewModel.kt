package com.qttx.kedouhulian.ui.trade.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.bean.DescriptionBean
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.bean.TradeMarketBean
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class TradeDetailViewModel constructor(val api: Api) : BaseViewModel() {

    val detailLiveData = NetLiveData<TradeMarketBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    val buyLiveData = NetLiveData<PayBean>()

    fun getData(owner: LifecycleOwner, id: String) {
        api
                .sell_info(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(detailLiveData)
    }

    fun buyData(owner: LifecycleOwner) {
        detailLiveData.value
                ?.data?.let {
            val map = mutableMapOf<String, String>()
            map["trade_id"] = it.id.toString()
            map["buy_num"] = it.seller_num.toString()
            map["unit"] = it.unit
            map["seller_id"] = it.uid.toString()
            api
                    .trade_order(map)
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(buyLiveData)

        }
    }
    val buyPayLiveData = NetLiveData<PayBean>()
    fun buyDataPay(owner: LifecycleOwner) {
        detailLiveData.value
                ?.data?.let {
            val map = mutableMapOf<String, String>()
            map["trade_id"] = it.id.toString()
            map["buy_num"] = it.seller_num.toString()
            map["unit"] = it.unit
            map["seller_id"] = it.uid.toString()
            api
                    .trade_order(map)
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(buyPayLiveData)

        }
    }

    val descriptionLiveData = NetLiveData<DescriptionBean>()

    fun descriptionData(owner: LifecycleOwner) {

        api.walletUse().bindSchedulerExceptionLife(owner)
                .subscribe(descriptionLiveData)
    }
}
