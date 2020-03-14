package com.qttx.kedouhulian.ui.wallet

import android.content.Intent
import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.BaseTextWatcher
import kotlinx.android.synthetic.main.wallet_activity_cash.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/16 0016
 */
class WalletCashActivity : BaseActivity() {

    private val viewModel: WalletViewModel by viewModel()

    private var payType = 1

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_cash
    }

    private var availableAmount: Double = 0.0

    private lateinit var textWatcher: BaseTextWatcher

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("提现")
        availableAmount = intent.getDoubleExtra("number", 0.0)
        balanceTv.text = "可提现资金：${availableAmount}元"

        textWatcher = BaseTextWatcher().setMax(availableAmount)
        viewModel.cashAccount(this)
        moneyEt.addTextChangedListener(textWatcher)
        setPayType()
        pay_alipay_ll.setOnClickListener {
            payType = 1
            setPayType()
        }
        pay_weixin_ll.setOnClickListener {
            payType = 2
            setPayType()
        }
        pay_bank_ll.setOnClickListener {
            payType = 3
            setPayType()
        }

        post_pay.setOnClickListener {
            pay()
        }
        alipayacountButton.setOnClickListener {
            viewModel.cashAccountLiveData.value?.data?.let {
                val intent = Intent(this, WalletAcountAliActivity::class.java)
                intent.putExtra("bean", it)
                startActivityForResult(intent, 200)
            }

        }
        wxacountButton.setOnClickListener {
            viewModel.cashAccountLiveData.value?.data?.let {
                val intent = Intent(this, WalletAcountWxActivity::class.java)
                intent.putExtra("bean", it)
                startActivityForResult(intent, 200)
            }
        }
        bankacountButton.setOnClickListener {
            viewModel.cashAccountLiveData.value?.data?.let {
                val intent = Intent(this, WalletAcountBankActivity::class.java)
                intent.putExtra("bean", it)
                startActivityForResult(intent, 200)
            }
        }
    }

    override fun liveDataListener() {

        viewModel.cashAccountLiveData.toObservable(this)
        {
            it.data?.let {

                if (it.z_id == "0" || it.z_id.isEmpty()) {
                    alipayacountButton.text = "添加"
                    alipayacountTv.text = ""
                } else {
                    alipayacountButton.text = "修改"
                    alipayacountTv.text = it.z_number
                }
                if (it.w_id == "0" || it.w_id.isEmpty()) {
                    wxacountButton.text = "添加"
                    wxacountTv.text = ""
                } else {
                    wxacountButton.text = "修改"
                    wxacountTv.text = it.w_number
                }

                if (it.y_id == "0" || it.y_id.isEmpty()) {
                    bankacountButton.text = "添加"
                    bankacountTv.text = ""
                } else {
                    bankacountButton.text = "修改"
                    bankacountTv.text = it.y_number
                }

                balanceTv.text = "可提现资金：${it.money}元"
                textWatcher.setMax(it.money.toDouble())
            }
        }
        viewModel.applyCashLiveData.toObservable(this)
        {
            setResult(400)
            showToast("申请成功")
            finish()
        }
    }

    private fun setPayType() {
        when (payType) {
            1 -> {
                pay_alipay_iv.isSelected = true
                pay_weixin_iv.isSelected = false
                pay_bank_iv.isSelected = false
            }
            2 -> {
                pay_alipay_iv.isSelected = false
                pay_weixin_iv.isSelected = true
                pay_bank_iv.isSelected = false
            }
            3 -> {
                pay_alipay_iv.isSelected = false
                pay_weixin_iv.isSelected = false
                pay_bank_iv.isSelected = true
            }
        }
    }


    private fun pay() {

        val text = moneyEt.text.toString()
        if (text.isEmpty()) {
            showToast("请输入提现金额")
        } else {

            viewModel.cashAccountLiveData.value?.data?.let {

                val map = mutableMapOf<String, String>()
                map["money"] = text
                if (payType == 1) {
                    if (it.z_id == "0" || it.z_id.isEmpty()) {
                        showToast("请添加支付宝账号")
                        return
                    }
                    map["withdraw_id"] = it.z_id

                } else if (payType == 3) {
                    if (it.y_id == "0" || it.y_id.isEmpty()) {
                        showToast("请添加银行账号")
                        return
                    }
                    map["withdraw_id"] = it.y_id
                } else {
                    if (it.w_id == "0" || it.w_id.isEmpty()) {
                        showToast("请添加微信账号")
                        return
                    }
                    map["withdraw_id"] = it.w_id
                }
                viewModel.applyCash(this, map)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 400) {
            viewModel.cashAccount(this)
        }
    }

}