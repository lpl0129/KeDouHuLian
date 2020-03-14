package com.qttx.kedouhulian.ui.common

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.PayTask
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetObserver
import com.qttx.kedouhulian.ui.dialog.ChoseCityDilog
import com.qttx.kedouhulian.utils.EventFilterBean
import com.qttx.kedouhulian.utils.EventStatus
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog
import com.stay.toolslibrary.net.ViewStatus
import com.stay.toolslibrary.utils.ToastUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.livedata.toObservable
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
import kotlinx.android.synthetic.main.common_activity_pay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

/**
 * @author huangyr
 * @date 2019/4/16 0016
 */
class PayActivity : BaseNiceDialog() {


    override fun intLayoutId(): Int {
        return R.layout.common_activity_pay
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        payBean = arguments!!.getParcelable("bean")!!
    }

    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {
        EventBus.getDefault().register(this)
        if (payBean.data_id == "36") {
            tipTv.text = "开通公开群需支付${payBean.paymoney}元"
            tipTv.visibility = View.VISIBLE
        } else {
            tipTv.visibility = View.GONE
        }
        price_tv.text = "¥${payBean.paymoney}"
        setPayType()
        top_left.setOnClickListener {
            dismiss()
        }
        pay_alipay_ll.setOnClickListener {
            payType = alipay
            setPayType()
        }
        blacne_pay_ll.setOnClickListener {
            payType = usermoney
            setPayType()
        }
        pay_weixin_ll.setOnClickListener {
            payType = wechat
            setPayType()
        }

        pay_tv.setOnClickListener {
            pay()
        }
        spite.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        val alipay = "alipay"
        val wechat = "wechat"
        val usermoney = "usermoney"
        fun newInstance(bean: PayBean): PayActivity {
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            val dialog = PayActivity()
            dialog.setShowBottom(true)
            dialog.arguments = bundle
            return dialog
        }
    }

    private var payType = alipay

    private lateinit var payBean: PayBean
    val api: Api by inject()

    fun liveDataListener() {

    }


    private fun setPayType() {
        when (payType) {
            alipay -> {
                pay_alipay_iv.isSelected = true
                pay_weixin_iv.isSelected = false
                balance_iv.isSelected = false
            }
            wechat -> {
                pay_alipay_iv.isSelected = false
                pay_weixin_iv.isSelected = true
                balance_iv.isSelected = false
            }
            else -> {
                pay_alipay_iv.isSelected = false
                pay_weixin_iv.isSelected = false
                balance_iv.isSelected = true
            }
        }
    }

    private fun pay() {
        val map = mutableMapOf<String, String>()
        map["money"] = payBean.paymoney.toString()
        map["paytype"] = payType
        map["fromtype"] = payBean.fromtype.toString()
        map["data_id"] = payBean.data_id
        api.recharge_submit(map)
                .bindSchedulerExceptionLife(this)
                .subscribe(NetObserver {

                    if (getNetMsgBean().status == ViewStatus.SUCCESS) {
                        when (payType) {
                            alipay -> {
                                data?.alipay?.let {
                                    if (checkAliPayInstalled()) {
                                        alipay(it)
                                    } else {
                                        ToastUtils.showShort("您未安装支付宝~")
                                    }
                                }

                            }
                            wechat -> {
                                data?.wechat?.let {
                                    weixinPay(it)
                                }
                            }
                            else -> {
                                onPaySuccess()
                            }
                        }
                    } else if (getNetMsgBean().status == ViewStatus.ERROR_TOAST) {
                        ToastUtils.showShort(getNetMsgBean().errorMsg)
                    }

                }

                )


    }

    private fun alipay(orderinfo: String) {

        Observable.create(ObservableOnSubscribe<Map<String, String>> { emitter ->
            val alipay = PayTask(activity)
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

    private fun checkAliPayInstalled(): Boolean {
        val uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(com.stay.toolslibrary.utils.extension.getApplicationContext().packageManager)
        return componentName != null
    }

    private fun onPaySuccess() {
        "支付成功".showToast()
        payResult?.onPaySuccess()
        dismiss()
    }

    var payResult: PayResult? = null

    fun setListener(payResult: PayResult): PayActivity {
        this@PayActivity.payResult = payResult
        return this
    }

    private fun onPayFailed(text: String) {
        text.showToast()
        dismiss()
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
        val api = WXAPIFactory.createWXAPI(context, bean.appid)
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
}