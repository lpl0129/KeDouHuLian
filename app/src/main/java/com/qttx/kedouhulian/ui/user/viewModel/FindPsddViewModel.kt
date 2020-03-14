package com.qttx.kedouhulian.ui.user.viewModel




import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.BaseViewModel
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.showToast
import java.util.regex.Pattern

class FindPsddViewModel constructor(val api: Api) : BaseViewModel() {
    var reSetliveData = NetLiveData<Any>()

    fun findpasd(own: LifecycleOwner, phone: String, captcha: String, password: String) {
        if (TextUtils.isEmpty(phone)) {
            "请输入手机号".showToast()
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            "手机号格式不正确".showToast()
            return
        }

        if (TextUtils.isEmpty(captcha)) {
            "请输入短信验证码".showToast()
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
        val USERNAME_PATTERN = "^[0-9a-zA-Z_]{1,}$"
        val pattern = Pattern.compile(USERNAME_PATTERN)
        val matcher = pattern.matcher(password)
        if (!matcher.matches()) {
            "密码格式不正确(数字、字母或下划线)".showToast()
            return
        }

        val params = mutableMapOf<String, String>()
        params["mobile"] = phone
        params["captcha"] = captcha
        params["newpassword"] = password

        api.resetpwd(params)
            .bindSchedulerExceptionLife(own)
            .subscribe(reSetliveData)
    }
}
