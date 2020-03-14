package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class ChatApplyViewModel constructor(val api: Api,var group_id:String) : BaseListViewModel<ChatApplyBean>() {

    val managerLiveData = NetLiveData<Any>()

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<ChatApplyBean>>> {
        return if (TextUtils.isEmpty(group_id))
        {
            api.friends_msg_list(params)
        }else
        {
            api.group_msg_list(params)
        }
    }

    var userid=""
    fun agreeApply(owner: LifecycleOwner, id: String) {
        if (TextUtils.isEmpty(group_id))
        {
            api.friends_operate(id,"1")
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(managerLiveData)
        }else
        {

            api.group_msg_operate(id,"1")
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(managerLiveData)

        }
    }


}
