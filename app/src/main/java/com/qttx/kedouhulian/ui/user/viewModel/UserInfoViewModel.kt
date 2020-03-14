package com.qttx.kedouhulian.ui.user.viewModel

import android.arch.lifecycle.LifecycleOwner
import android.net.Uri
import android.text.TextUtils
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.PhoneUtils
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerException
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.showToast
import io.rong.imkit.RongIM
import io.rong.imlib.model.Group

/**
 * @author huangyr
 * @date 2019/5/14 0014
 */
class UserInfoViewModel constructor(val api: Api) : BaseViewModel() {

    var avatarLocal: String = ""

    val userinfoLiveData = NetLiveData<UserInfoById>()

    fun getUserInfoById(own: LifecycleOwner, id: String) {
        api.getUserInfoById(id)
                .bindSchedulerExceptionLife(own)
                .subscribe(userinfoLiveData)
    }

    val userinfo2LiveData = NetLiveData<UserInfoById>()
    fun getUserInfo(own: LifecycleOwner) {
        api.getUserInfo()
                .bindSchedulerExceptionLife(own)
                .subscribe(userinfo2LiveData)
    }


    val tokenLiveData = NetLiveData<String>()
    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }


    val editUserinfoLiveData = NetLiveData<Any>()

    fun editUserinfo(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.editProfile(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(editUserinfoLiveData)
    }


    val qrCodeLiveData = NetLiveData<QrCodeBean>()

    fun getQrCodeInfo(owner: LifecycleOwner) {
        api.myQcode()
                .bindSchedulerExceptionLife(owner)
                .subscribe(qrCodeLiveData)
    }

    val bonusInfoLiveData = NetLiveData<BonusBean>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)

    fun getBonusInfo(owner: LifecycleOwner) {
        api.share_red_info()
                .bindSchedulerExceptionLife(owner)
                .subscribe(bonusInfoLiveData)
    }

    val getbonusInfoLiveData = NetLiveData<BonusBean>()

    fun getBonus(owner: LifecycleOwner) {
        api.share_red()
                .bindSchedulerExceptionLife(owner)
                .subscribe(getbonusInfoLiveData)
    }

    val complainliveData = NetLiveData<Any>()

    fun compalin(owner: LifecycleOwner, mobile: String, appeal: String) {

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

        api.advice(mobile, appeal)
                .bindSchedulerExceptionLife(owner)
                .subscribe(complainliveData)
    }

    val versionLiveData = NetLiveData<VersionBean>()

    fun getAppVersion(owner: LifecycleOwner) {
        val version = PhoneUtils.getAppVersionCode()
        api.check_version(version, "android")
                .bindSchedulerExceptionLife(owner)
                .subscribe(versionLiveData)
    }


    fun compalinBounty(owner: LifecycleOwner, order_id: String, b_uid: String, mobile: String, appeal: String) {

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

        val map = mutableMapOf<String, String>()
        map["order_id"] = order_id
        map["b_uid"] = b_uid
        map["mobile"] = mobile
        map["content"] = appeal

        api.set_bounty_appeal(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(complainliveData)
    }
    val serverliveData = NetLiveData<String>()
    fun getServerdata(owner: LifecycleOwner)
    {
        api.get_customer()
                .bindSchedulerExceptionLife(owner )
                .subscribe(serverliveData)
    }
}