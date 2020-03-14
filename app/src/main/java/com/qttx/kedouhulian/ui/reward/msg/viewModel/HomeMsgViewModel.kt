package com.qttx.kedouhulian.ui.reward.msg.viewModel


import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.BaseListViewModel
import io.reactivex.Observable

class HomeMsgViewModel constructor(val api: Api) : BaseListViewModel<HomeMsgBean>() {


    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<HomeMsgBean>>> {
        return api.msgList(params)
    }


}
