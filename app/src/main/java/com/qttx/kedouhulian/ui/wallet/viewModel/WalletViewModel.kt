package com.qttx.kedouhulian.ui.wallet.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.DescriptionBean
import com.qttx.kedouhulian.bean.PayResultBean
import com.qttx.kedouhulian.bean.WalletAcountBean
import com.qttx.kedouhulian.bean.WalletBean
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.net.ViewStatus
import com.stay.toolslibrary.utils.ToastUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class WalletViewModel constructor(val api: Api) : BaseViewModel() {

    val aountLiveData = NetLiveData<WalletBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    fun getData(owner: LifecycleOwner) {
        api.myAccount()
                .bindSchedulerExceptionLife(owner)
                .subscribe(aountLiveData)
    }

    val payLiveData = NetLiveData<PayResultBean>()

    fun pay(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.recharge_submit(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(payLiveData)
    }


    val cashAccountLiveData = NetLiveData<WalletAcountBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    fun cashAccount(owner: LifecycleOwner) {
        api.account()
                .bindSchedulerExceptionLife(owner)
                .subscribe(cashAccountLiveData)
    }

    val modifyAcountLiveData = NetLiveData<Any>()

    fun modifyAcount(owner: LifecycleOwner, map: MutableMap<String, String>) {

        if (map.containsKey("ids")) {
            api.accountAmend(map)
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(modifyAcountLiveData)
        } else {
            api.accountBinding(map)
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(modifyAcountLiveData)
        }

    }

    val applyCashLiveData = NetLiveData<Any>()

    fun applyCash(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.apply_withdraw(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(applyCashLiveData)
    }

    val descriptionLiveData = NetLiveData<DescriptionBean>()

    fun descriptionData(owner: LifecycleOwner) {

        api.wallet_walletUse().bindSchedulerExceptionLife(owner)
                .subscribe(descriptionLiveData)
    }


    val transferLiveData = NetLiveData<Any>()

    public var hasSendMoney:String ="";
    fun transter_money(owner: LifecycleOwner, money: String, touid: String) {
        hasSendMoney=money;
        api.transter_money(money, touid).bindSchedulerExceptionLife(owner)
                .subscribe(transferLiveData)
    }


}
