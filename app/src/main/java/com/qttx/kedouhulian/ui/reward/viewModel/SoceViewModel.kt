package com.qttx.kedouhulian.ui.reward.viewModel

import android.arch.lifecycle.LifecycleOwner
import android.net.Uri
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerException
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.rong.imkit.RongIM
import io.rong.imlib.model.Group
import retrofit2.http.POST

/**
 * @author huangyr
 * @date 2019/5/14 0014
 */
class SoceViewModel constructor(val api: Api) : BaseViewModel() {


    val listLiveData = NetLiveData<NetResultListBean<ScoreBean>>(ViewLoadingStatus.LOADING_NO)

    fun getListData(owner: LifecycleOwner) {
        api.score_list()
                .bindSchedulerExceptionLife(owner)
                .subscribe(listLiveData)
    }

    val clearLiveData = NetLiveData<Any>()

    fun clearSroce(owner: LifecycleOwner) {
        api.getScore()
                .bindSchedulerExceptionLife(owner)
                .subscribe(clearLiveData)
    }
    val userinfo2LiveData = NetLiveData<UserInfoById>(ViewLoadingStatus.LOADING_NO)
    fun getUserInfo(own: LifecycleOwner) {
        api.getUserInfo()
                .bindSchedulerExceptionLife(own)
                .subscribe(userinfo2LiveData)
    }
    val gongLvLiveData = NetLiveData<GongLvBean>()

    fun getGongLv(owner: LifecycleOwner) {
        api.kedoubi()
                .bindSchedulerExceptionLife(owner)
                .subscribe(gongLvLiveData)
    }


}