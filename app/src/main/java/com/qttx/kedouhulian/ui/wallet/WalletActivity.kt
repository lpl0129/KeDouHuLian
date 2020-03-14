package com.qttx.kedouhulian.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.wallet_activity.*


class WalletActivity : BaseActivity() {

    private val viewModel: WalletViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.wallet_activity


    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("我的钱包")
        viewModel.getData(this)
        viewModel.descriptionData(this)
    }

    override fun liveDataListener() {
        viewModel.descriptionLiveData.toObservable(this)
        {
            it.data?.let {
                descriptionTv.text = Html.fromHtml(it.content)
            }
        }
        viewModel.aountLiveData.toObservable(this)
        {
            it.data?.let {
                balanceTv.text = "余额: ${it.money}元"
                dongjieTv.text = "冻结资金：${it.money_frozen}元"
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            payTv -> {
                viewModel.aountLiveData.value?.data?.let {

                    val intent = Intent(this, WalletPayActivity::class.java)
                    intent.putExtra("number",it.money.toDouble())
                    startActivityForResult(intent,200)
                }
            }
            cashTv -> {
                viewModel.aountLiveData.value?.data?.let {
                    val intent = Intent(this, WalletCashActivity::class.java)
                    intent.putExtra("number",it.money.toDouble())
                    startActivityForResult(intent,200)
                }


            }
            balanceLl -> {
            }
            kedouListLl -> {
                val intent = Intent(this, WalKeDoulAesstListActivity::class.java)
                startActivityForResult(intent,300)
            }
            aestListLl -> {

                val intent = Intent(this, WallAesstListActivity::class.java)
                startActivityForResult(intent,300)
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getData(this)
    }

    override fun initViewClickListeners() {
        payTv.setOnClickListener(this)
        cashTv.setOnClickListener(this)
        balanceLl.setOnClickListener(this)
        dongjieLl.setOnClickListener(this)
        kedouListLl.setOnClickListener(this)
        aestListLl.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 400) {
            viewModel.getData(this)
        }
    }
}
