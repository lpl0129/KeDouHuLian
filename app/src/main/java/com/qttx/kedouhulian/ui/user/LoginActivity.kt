package com.qttx.kedouhulian.ui.user

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.google.gson.Gson
import com.qttx.kedouhulian.App
import com.qttx.kedouhulian.ui.common.MainActivity
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.Userinfo
import com.qttx.kedouhulian.ui.user.viewModel.LoginViewModel
import com.qttx.kedouhulian.utils.*
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.*
import com.stay.toolslibrary.widget.dialog.TipDialog
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.user_activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/2 0002
 */
class LoginActivity : BaseActivity() {

    val viewModel: LoginViewModel  by viewModel()
    /**
     * 微信
     */
    private val WEIXIN_SCOPE = "snsapi_userinfo"
    private val WEIXIN_STATE = "login_state"
    private lateinit var wxApi: IWXAPI
    /**
     * 微博
     */
    private var mSsoHandler: SsoHandler? = null
    var pers = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
    override fun liveDataListener() {
        viewModel.loginLiveData.toObservable(this)
        {
            it.data?.let {
                if (it.whether_info == 0) {
                    //完善信息
                    val intent = Intent(this@LoginActivity, PerfectUserDataActivity::class.java)
                    intent.putExtra("user", it)
                    startActivityForResult(intent, 200)
                } else {
                    loginSuccess(it)
                }
            }
        }
        viewModel.wxUserinfoLiveData.toObservable(this)
        {
            it.data?.openid?.let { id ->
                val gson = Gson()
                val json = gson.toJson(it)
                viewModel.code = id
                viewModel.platform = "wechat"
                viewModel.json = json
                viewModel.thirdPartLogin(this)
            }
        }

        viewModel.thirdloginLiveData.toObservable(this)
        {
            if (it.code == 0) {
                //跳转绑定
                val intent = Intent(this@LoginActivity, BindPhoneActivity::class.java)
                intent.putExtra("code", viewModel.code)
                intent.putExtra("json", viewModel.json)
                intent.putExtra("platform", viewModel.platform)
                startActivityForResult(intent, 300)
            } else {
                it.data?.let {
                    if (it.whether_info == 0) {
                        //完善信息
                        val intent = Intent(this@LoginActivity, PerfectUserDataActivity::class.java)
                        intent.putExtra("user", it)
                        startActivityForResult(intent, 200)
                    } else {
                        loginSuccess(it)
                    }
                }
            }
        }

        viewModel.weiboLiveData
                .toObservable(this)
                {

                    it.data?.let {
                        val gson = Gson()
                        val json = gson.toJson(it)
                        viewModel.json = json
                        viewModel.thirdPartLogin(this@LoginActivity)
                    }
                }
    }

    var isShowPsw = false

    override fun getLayoutId(): Int = R.layout.user_activity_login

    override fun processLogic(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this);
        val logout = intent.getBooleanExtra("logout", false)
        if (logout) {
            TipDialog.newInstance("您的账号在其他设备登录", isTip = true)
                    .show(supportFragmentManager)
        }

        wxApi = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID, false)
        wxApi.registerApp(Constants.WXAPP_ID)
        val authInfo = AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE)
        WbSdk.install(this, authInfo)
        mSsoHandler = SsoHandler(this@LoginActivity)
    }

    /**
     *
     */
    override fun initViewClickListeners() {
        seePsdIv.setOnClickListener(this)
        findPsdTv.setOnClickListener(this)
        loginTv.setOnClickListener(this)
        registerTv.setOnClickListener(this)
        loginWeiXin.setOnClickListener(this)
        loginQQ.setOnClickListener(this)
        loginWeiBo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            seePsdIv -> {
                showPswOrNot()
            }
            findPsdTv -> {
                val intent = Intent(this, FindPsdActivity::class.java)
                startActivity(intent)
            }
            loginTv -> {

                if (hasPermissions(this@LoginActivity,pers)==null)
                {
                    viewModel.login(this, phoneEditText.text.toString(), psdEditText.text.toString())
                }else
                {
                    val intent = Intent(this, UserPerActivity::class.java)
                    startActivity(intent)
                }


            }
            registerTv -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivityForResult(intent, 200)
            }
            loginWeiXin -> {
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                val isinstalled = wxApi.isWXAppInstalled
                if (!isinstalled) {
                    ToastUtils.showShort("您未安装微信~")
                    return
                }
                val isPaySupported = wxApi.wxAppSupportAPI >= Build.PAY_SUPPORTED_SDK_INT
                if (isPaySupported) {
                    val req = SendAuth.Req()
                    req.scope = WEIXIN_SCOPE
                    req.state = WEIXIN_STATE
                    wxApi.sendReq(req)
                } else {
                    "微信版本过低,请升级".showToast()
                }

            }
            loginQQ -> {
            }
            loginWeiBo -> {
                mSsoHandler?.authorize(
                        object : WbAuthListener {
                            override fun cancel() {
                                "取消登录".showToast()
                            }

                            override fun onFailure(p0: WbConnectErrorMessage?) {
                                "登录失败${p0?.errorMessage}".showToast()
                            }

                            override fun onSuccess(p0: Oauth2AccessToken?) {
                                p0?.let {
                                    if (it.isSessionValid) {
                                        AccessTokenKeeper.writeAccessToken(applicationContext, it)
                                        viewModel.code = it.uid
                                        viewModel.platform = "weibo"
                                        viewModel.getWeiBoUserInfo(this@LoginActivity, it.token, it.uid)
                                    }
                                }
                            }
                        }
                )
            }
        }
    }

    /**
     * 密码是否明文显示
     */
    private fun showPswOrNot() {
        isShowPsw = !isShowPsw
        if (isShowPsw) {
            psdEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            psdEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        psdEditText.setSelection(psdEditText.text.length)
        seePsdIv.setImageResource(if (isShowPsw) R.drawable.zhengyanjing_btn else R.drawable.biyanjing_btn)
    }

    /**
     * 微信登录回调
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(filter: EventFilterBean) {
        val type = filter.type
        when (type) {
            EventStatus.WEIXIN_LOGIN -> {
                val code = filter.value.toString()
                viewModel.getWeixinInfo(this, code)
            }
        }
    }


    /**
     * 登录成功,连接融云服务器
     */
    fun loginSuccess(userinfo: Userinfo) {
        JPushInterface.setAlias(applicationContext, 1, userinfo.user_id)
        showToast("登录成功")
        KeyboardUtils.hideSoftInput(this@LoginActivity)
        setToken(userinfo.token)
        setRongToken(userinfo.rongyun_token)
        setUserPhone(userinfo.mobile)
        setUserId(userinfo.user_id)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()


       // 融云暂未使用 先注释
//        showLoadingDilog("正在登陆...")
//        if (applicationInfo.packageName == App.getProcessName(applicationContext)) {
//            RongIM.connect(userinfo.rongyun_token, object : RongIMClient.ConnectCallback() {
//                override fun onTokenIncorrect() {
//                    runOnUiThread {
//                        dimissLoadingDialog()
//                        showToast("登录失败")
//                    }
//                }
//
//                /**
//                 * 连接融云成功
//                 *
//                 * @param userid 当前 token 对应的用户 id
//                 */
//                override fun onSuccess(userid: String) {
//                    dimissLoadingDialog()
//                    JPushInterface.setAlias(applicationContext, 1, userinfo.user_id)
//                    showToast("登录成功")
//                    KeyboardUtils.hideSoftInput(this@LoginActivity)
//                    setToken(userinfo.token)
//                    setRongToken(userinfo.rongyun_token)
//                    setUserPhone(userinfo.mobile)
//                    setUserId(userinfo.user_id)
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//
//                /**
//                 * 连接融云失败
//                 *
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                override fun onError(errorCode: RongIMClient.ErrorCode) {
//                    dimissLoadingDialog()
//                    showToast("登录失败")
//                }
//            })
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
        if (resultCode == 400) {
            val userbean: Userinfo? = data?.getParcelableExtra("bean")
            userbean?.let {
                loginSuccess(it)
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}