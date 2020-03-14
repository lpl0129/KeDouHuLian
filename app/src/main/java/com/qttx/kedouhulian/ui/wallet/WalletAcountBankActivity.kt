package com.qttx.kedouhulian.ui.wallet

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.WalletAcountBean
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_acount_bank.*
import kotlinx.android.synthetic.main.wallet_activity_acount_wx.*
import kotlinx.android.synthetic.main.wallet_activity_acount_wx.acountEt
import kotlinx.android.synthetic.main.wallet_activity_acount_wx.acountNameEt
import kotlinx.android.synthetic.main.wallet_activity_acount_wx.post_pay
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/17 0017
 */
class WalletAcountBankActivity : BaseActivity() {
    private val viewModel: WalletViewModel by viewModel()
    private lateinit var acountBean: WalletAcountBean
    val map = mutableMapOf<String, String>()
    
    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_acount_bank
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        acountBean = intent.getParcelableExtra("bean")
        if (acountBean.y_id == "0" || acountBean.y_id.isEmpty()) {
            //无账号
            setTopTitle(" 绑定银行卡")
        } else {
            setTopTitle("修改银行卡")
            map["ids"] = acountBean.y_id
            acountNameEt.setText(acountBean.y_realname)
            acountEt.setText(acountBean.y_number)
            bankNameEt.setText(acountBean.y_bankname)
        }
        post_pay.setOnClickListener {
            val textname = acountNameEt.text.toString()
            val text = acountEt.text.toString()
            val bankname = bankNameEt.text.toString()
            when {
                textname.isEmpty() -> showToast("请输入姓名")
                text.isEmpty() -> showToast("请输入银行账号")
                bankname.isEmpty() -> showToast("请输入您的开户行名称")
                else -> {
                    map["sign"] = "3"
                    map["name"] = textname
                    map["account_number"] = text
                    map["bank_name"] = bankname
                    viewModel.modifyAcount(this, map)
                }
            }
        }
    }

    override fun liveDataListener() {
        viewModel.modifyAcountLiveData.toObservable(this)
        {
            setResult(400)
            finish()
        }
    }

}
