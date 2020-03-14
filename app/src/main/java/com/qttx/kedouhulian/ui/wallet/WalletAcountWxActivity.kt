package com.qttx.kedouhulian.ui.wallet

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.WalletAcountBean
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_acount_wx.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/17 0017
 */
class WalletAcountWxActivity : BaseActivity() {
    private val viewModel: WalletViewModel by viewModel()
    private lateinit var acountBean: WalletAcountBean
    val map = mutableMapOf<String, String>()
    
    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_acount_wx
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        acountBean = intent.getParcelableExtra("bean")
        if (acountBean.w_id == "0" || acountBean.w_id.isEmpty()) {
            //无账号
            setTopTitle("绑定微信")
        } else {
            setTopTitle("修改微信")
            map["ids"] = acountBean.w_id
            acountNameEt.setText(acountBean.w_realname)
            acountEt.setText(acountBean.w_number)
        }

        post_pay.setOnClickListener {
            val textname = acountNameEt.text.toString()
            val text = acountEt.text.toString()

            when {
                textname.isEmpty() -> showToast("请输入姓名")
                text.isEmpty() -> showToast("请输入微信账号")
                else -> {
                    map["sign"] = "2"
                    map["name"] = textname
                    map["account_number"] = text
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
