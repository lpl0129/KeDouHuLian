package com.qttx.kedouhulian.ui.trade.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.lxj.xpopup.widget.LoadingView
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.bean.TradeConfigBean
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class TradePublishViewModel constructor(val api: Api) : BaseViewModel() {

    var isTop = 0

    var isAgree = 1
    val buyLiveData = NetLiveData<PayBean>()

    val configLiveData = NetLiveData<TradeConfigBean>(ViewLoadingStatus.LOADING_VIEW,ViewErrorStatus.ERROR_VIEW)

    fun saveData(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.sell_add(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(buyLiveData)
    }

    fun getConfig(owner: LifecycleOwner) {
        api.trade_get_setting()
                .bindSchedulerExceptionLife(owner)
                .subscribe(configLiveData)
    }
}
