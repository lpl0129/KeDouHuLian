package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable

class ChatBountyViewModel constructor(val api: Api) : BaseListViewModel<BountyUserBean>() {

    init {
        pageSize = 40
    }

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyUserBean>>> {
        return api.bounty_order_list1(params)
    }

    /**
     * 获取悬赏群成员
     */
    val bountyChangeLiveData = NetLiveData<Any>()
    val bountyjieguLiveData = NetLiveData<Any>()
    var pos = 0
    fun bountyChange(owner: LifecycleOwner, bounty_order_id: String, uid: String, position: Int, state: Int) {

        pos = position
        when (state) {
            0 -> {
                api.bounty_employ(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyjieguLiveData)
            }
            1 -> {
                api.bounty_fire(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyChangeLiveData)
            }
            2 -> {
                api.bounty_pay(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyChangeLiveData)
            }
        }

    }


}
