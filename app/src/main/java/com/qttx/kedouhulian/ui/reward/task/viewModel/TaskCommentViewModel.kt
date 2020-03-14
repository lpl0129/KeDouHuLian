package com.qttx.kedouhulian.ui.reward.task.viewModel

import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class TaskCommentViewModel constructor(val api: Api) : BaseViewModel() {

    val commitLiveData = NetLiveData<Any>()

    fun submitConent(owner: LifecycleOwner, content: String, id: String) {
        api.addreply(id, content)
                .bindSchedulerExceptionLife(owner)
                .subscribe(commitLiveData)
    }

    fun submitConent_bounty(owner: LifecycleOwner, content: String, id: String) {
        api.bounty_reply_add(id, content)
                .bindSchedulerExceptionLife(owner)
                .subscribe(commitLiveData)
    }

    /**
     * 回复留言
     */
    fun evaluate(owner: LifecycleOwner, content: String, id: String,b_id:String) {
        val map= mutableMapOf<String,String>()
        //b_id为0时 ,说明是回复,但是是直接回复留言,没有对谁说
        if(!TextUtils.isEmpty(b_id)|| "0" != b_id)
        {
            map["b_id"]=b_id
        }
        map["id"]=id
        map["content"]=content

        api.evaluate(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(commitLiveData)
    }
    /**
     * 回复留言
     */
    fun evaluateBounty(owner: LifecycleOwner, content: String, id: String,b_id:String) {
        val map= mutableMapOf<String,String>()
        //b_id为0时 ,说明是回复,但是是直接回复留言,没有对谁说
        if(!TextUtils.isEmpty(b_id)|| "0" != b_id)
        {
            map["b_id"]=b_id
        }
        map["id"]=id
        map["content"]=content

        api.bountyevaluate(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(commitLiveData)
    }
}
