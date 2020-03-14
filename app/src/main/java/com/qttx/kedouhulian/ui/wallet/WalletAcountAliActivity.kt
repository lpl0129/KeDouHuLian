package com.qttx.kedouhulian.ui.wallet

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.WalletAcountBean
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_acount_ali.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/17 0017
 */
class WalletAcountAliActivity : BaseActivity() {
    private val viewModel: WalletViewModel by viewModel()
    private lateinit var acountBean: WalletAcountBean
    val map = mutableMapOf<String, String>()

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_acount_ali
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        acountBean = intent.getParcelableExtra("bean")
        if (acountBean.z_id == "0" || acountBean.z_id.isEmpty()) {
            //无账号
            setTopTitle("绑定支付宝")
        } else {
            setTopTitle("修改支付宝")
            map["ids"] = acountBean.z_id
            acountNameEt.setText(acountBean.z_realname)
            acountEt.setText(acountBean.z_number)
        }

        post_pay.setOnClickListener {
            val textname = acountNameEt.text.toString()
            val text = acountEt.text.toString()

            when {
                textname.isEmpty() -> showToast("请输入姓名")
                text.isEmpty() -> showToast("请输入支付宝账号")
                else -> {
                    map["sign"] = "1"
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
