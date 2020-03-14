package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class ChatGroupViewModel constructor(val api: Api) : BaseListViewModel<GroupListBean>() {

    init {
        pageSize = 10000000
    }


    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<GroupListBean>>> {

        return api.group_list(params)
    }


}
