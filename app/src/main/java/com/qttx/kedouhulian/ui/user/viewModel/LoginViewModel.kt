package com.qttx.kedouhulian.ui.user.viewModel

import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.net.NetObserver
import com.qttx.kedouhulian.utils.Constants
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.net.SingleLiveEvent
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.extension.bindSchedulerLife
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.utils.showToast
import com.zhihu.matisse.internal.utils.Platform
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.rong.imlib.statistics.UserData.phone
import org.koin.standalone.inject

/**
 * @author huangyr
 * @date 2019/4/2 0002
 */
class LoginViewModel constructor(val api: Api) : BaseViewModel() {

    var wxUserinfoLiveData = NetLiveData<WeiXinUserInfoBean>()

    var loginLiveData = NetLiveData<Userinfo>()
    var thirdloginLiveData = NetLiveData<Userinfo>()
    var weiboLiveData = NetLiveData<WeiBoUserInfoBean>()

    var platform = "weibo"
    var code: String = ""
    var json: String = ""

    /**
     * 获取微信用户信息
     */
    fun getWeixinInfo(owner: LifecycleOwner, code: String) {
        val WXAPP_ID = Constants.WXAPP_ID
        val WXAPP_SECRET = Constants.WXAPP_SECRET
        val path =
                ("https://api.weixin.qq.com/sns/oauth2/access_token?appid=$WXAPP_ID&secret=$WXAPP_SECRET&code=$code&grant_type=authorization_code")
        api.getWeiXinAccessToken(path)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    val accessToken = it.access_token
                    val openId = it.openid
                    val path = ("https://api.weixin.qq.com/sns/userinfo?access_token="
                            + accessToken + "&openid=" + openId)
                    return@flatMap api.getWeiXinUserInfo(path)
                }
                .map {
                    val result = NetResultBean<WeiXinUserInfoBean>()
                    result.data = it
                    result
                }
                .observeOn(AndroidSchedulers.mainThread())
                .bindLifecycle(owner)
                .subscribe(wxUserinfoLiveData)
    }

    /**
     * 三方登录
     */
    fun thirdPartLogin(owner: LifecycleOwner) {
        api.thirdLogin(platform, code)
                .bindSchedulerLife(owner)
                .subscribe(thirdloginLiveData)
    }

    /**
     * 登录
     */
    fun login(owner: LifecycleOwner, phone: String, password: String) {

        if (TextUtils.isEmpty(phone)) {
            "请输入手机号".showToast()
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            "手机号格式不正确".showToast()
            return
        }

        if (TextUtils.isEmpty(password)) {
            "密码不能为空".showToast()
            return
        }

        if (password.length < 6 || password.length > 20) {
            "请输入6到20位密码".showToast()
            return
        }
        api.login(phone, password)
                .bindSchedulerExceptionLife(owner)
                .subscribe(loginLiveData)
    }


    fun getWeiBoUserInfo(owner: LifecycleOwner, access_token: String, uid: String) {
        val path = ("https://api.weibo.com/2/users/show.json?access_token=$access_token&uid=$uid")
        api
                .getWeiBoUserInfo(path)
                .map {
                    val result = NetResultBean<WeiBoUserInfoBean>()
                    result.data = it
                    result
                }
                .bindSchedulerLife(owner)
                .subscribe(weiboLiveData)

    }

}