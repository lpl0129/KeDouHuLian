package com.qttx.kedouhulian.ui.wallet.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class WalletListViewModel constructor(val api: Api) : BaseListViewModel<WalletListBean>() {

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<WalletListBean>>> {

        return api.myAccountList(params)

    }


}
