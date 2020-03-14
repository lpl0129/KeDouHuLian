package com.qttx.kedouhulian.ui.common.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.PayResultBean
import com.qttx.kedouhulian.bean.VersionBean
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.utils.PhoneUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

/**
 * @author huangyr
 * @date 2019/4/16 0016
 */
class PayViewModel constructor(val api: Api) : BaseViewModel() {
    val payLiveData = NetLiveData<PayResultBean>()

     fun pay(owner: LifecycleOwner, map: Map<String, String>) {
        api.recharge_submit(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(payLiveData)
    }
    val versionLiveData = NetLiveData<VersionBean>()

    fun getAppVersion (owner: LifecycleOwner)
    {
        val version= PhoneUtils.getAppVersionCode()
        api.check_version(version,"android")
                .bindSchedulerExceptionLife(owner)
                .subscribe(versionLiveData)
    }
}