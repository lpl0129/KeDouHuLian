package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class ChatCustomMsgViewModel constructor(val api: Api) : BaseListViewModel<ChatCustomMsgBean>() {

    init {
        pageSize=50
    }

    val addLiveData = NetLiveData<Any>()

    val delLiveData = NetLiveData<Any>()

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<ChatCustomMsgBean>>> {
       return api.user_custom_list(params)
    }

    var pos=0

    fun delMsg(owner: LifecycleOwner, id: String,position:Int) {
        pos=position
        api.user_custom_del(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(delLiveData)
    }

    fun addMsg(owner: LifecycleOwner, content: String) {
        api.user_custom_add(content)
                .bindSchedulerExceptionLife(owner)
                .subscribe(addLiveData)
    }
}
