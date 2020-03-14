package com.qttx.kedouhulian.ui.reward.task.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.showToast
import io.rong.imlib.statistics.UserData.phone
import kotlinx.android.synthetic.main.reward_activity_task_complain.*

class TaskComplainViewModel constructor(val api: Api) : BaseViewModel() {


    val liveData = NetLiveData<Any>()

    fun getData(owner: LifecycleOwner, orderid: String, mobile: String, appeal: String) {

        if (TextUtils.isEmpty(mobile)) {
            "请输入手机号".showToast()
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            "手机号格式不正确".showToast()
            return
        }

        if (TextUtils.isEmpty(appeal)) {
            "请输入申诉内容".showToast()
            return
        }

        api.taskAppeal(orderid, mobile, appeal)
                .bindSchedulerExceptionLife(owner)
                .subscribe(liveData)
    }

}
