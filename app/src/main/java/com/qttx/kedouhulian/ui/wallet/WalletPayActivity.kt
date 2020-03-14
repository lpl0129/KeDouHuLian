package com.qttx.kedouhulian.ui.wallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import com.alipay.sdk.app.PayTask
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PayResult
import com.qttx.kedouhulian.bean.WeixinPayBean
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.qttx.kedouhulian.utils.EventFilterBean
import com.qttx.kedouhulian.utils.EventStatus
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.BaseTextWatcher
import com.stay.toolslibrary.utils.ToastUtils
import com.stay.toolslibrary.utils.showToast
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.common_activity_pay.pay_alipay_iv
import kotlinx.android.synthetic.main.common_activity_pay.pay_alipay_ll
import kotlinx.android.synthetic.main.common_activity_pay.pay_weixin_iv
import kotlinx.android.synthetic.main.common_activity_pay.pay_weixin_ll
import kotlinx.android.synthetic.main.wallet_activity_pay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/16 0016
 */
class WalletPayActivity : BaseActivity() {

    private val viewModel: WalletViewModel by viewModel()

    companion object {
        val alipay = "alipay"
        val wechat = "wechat"
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_pay
    }

    private var payType = alipay


    private var availableAmount: Double = 0.0

    override fun processLogic(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        setTopTitle("充值")
        availableAmount = intent.getDoubleExtra("number", 0.0)
        balanceTv.text = "当前剩余: ${availableAmount}元"

        moneyEt.addTextChangedListener(BaseTextWatcher())
        setPayType()
        pay_alipay_ll.setOnClickListener {
            payType = alipay
            setPayType()
        }
        pay_weixin_ll.setOnClickListener {
            payType = wechat
            setPayType()
        }
        post_pay.setOnClickListener {
            pay()
        }
    }

    override fun liveDataListener() {
        viewModel.payLiveData
                .toObservable(this)
                {
                    it.data?.let {
                        when (payType) {
                            PayActivity.alipay -> {
                                it.alipay?.let {
                                    if (checkAliPayInstalled()) {
                                        alipay(it)
                                    } else {
                                        ToastUtils.showShort("您未安装支付宝~")
                                    }
                                }

                            }
                            PayActivity.wechat -> {
                                it.wechat?.let {
                                    weixinPay(it)
                                }
                            }
                            else -> {
                                onPaySuccess()
                            }
                        }
                    }
                }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(filter: EventFilterBean) {
        if (EventStatus.WEIXIN_PAY == filter.type) {
            val type = Integer.parseInt(filter.value.toString())
            if (type == 0) {
                onPaySuccess()
            } else {
                onPayFailed("取消支付")
            }
        }

    }
    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
    private fun weixinPay(bean: WeixinPayBean) {
        val req = PayReq()
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId = bean.appid
        req.partnerId = bean.partnerid
        req.prepayId = bean.prepayid
        req.nonceStr = bean.noncestr
        req.timeStamp = bean.timestamp
        req.packageValue = bean.packageX
        req.sign = bean.sign
        req.extData = "1"
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        val api = WXAPIFactory.createWXAPI(this, bean.appid)
        val isinstalled = api.isWXAppInstalled
        if (!isinstalled) {
            ToastUtils.showShort("您未安装微信~")
            return
        }
        val isPaySupported = api.wxAppSupportAPI >= Build.PAY_SUPPORTED_SDK_INT
        if (isPaySupported) {
            api.registerApp(bean.appid)
            api.sendReq(req)
        } else {
            "微信版本过低,请升级".showToast()
        }
    }
    private fun setPayType() {
        when (payType) {
            PayActivity.alipay -> {
                pay_alipay_iv.isSelected = true
                pay_weixin_iv.isSelected = false
            }
            PayActivity.wechat -> {
                pay_alipay_iv.isSelected = false
                pay_weixin_iv.isSelected = true
            }
        }
    }

    private fun alipay(orderinfo: String) {

        Observable.create(ObservableOnSubscribe<Map<String, String>> { emitter ->
            val alipay = PayTask(this)
            val result = alipay.payV2(orderinfo, true)
            emitter.onNext(result)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Map<String, String>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(map: Map<String, String>) {
                        val payResult = PayResult(map)
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        val resultInfo = payResult.result// 同步返回需要验证的信息
                        val resultStatus = payResult.resultStatus
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            //                        Utils.postEvent(Utils.EVENT_PAY_STATE, 0);
                            onPaySuccess()
                        } else if (!TextUtils.isEmpty(resultInfo)) {
                            onPayFailed(resultInfo)
                        } else {
                            onPayFailed("取消支付")
                        }
                    }

                    override fun onError(e: Throwable) {
                        onPayFailed(e.toString())
                    }

                    override fun onComplete() {

                    }
                })
    }

    private fun onPaySuccess() {
        "充值成功".showToast()
        setResult(400)
        finish()
    }

    private fun onPayFailed(text: String) {
        text.showToast()
    }

    private fun pay() {

        val text = moneyEt.text.toString()
        if (text.isEmpty()) {
            showToast("请输入充值金额")
        } else {
            val map = mutableMapOf<String, String>()
            map["money"] = text
            map["paytype"] = payType
            map["fromtype"] = "0"
            map["data_id"] = "0"

            viewModel.pay(this, map)
        }

    }

    private fun checkAliPayInstalled(): Boolean {
        val uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(com.stay.toolslibrary.utils.extension.getApplicationContext().packageManager)
        return componentName != null
    }


}