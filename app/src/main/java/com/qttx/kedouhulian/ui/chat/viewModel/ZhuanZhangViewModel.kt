package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.PayResultBean
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class ZhuanZhangViewModel constructor(val api: Api) : BaseViewModel() {

    val payLiveData = NetLiveData<PayResultBean>()
    fun pay(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.recharge_submit(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(payLiveData)
    }

}
