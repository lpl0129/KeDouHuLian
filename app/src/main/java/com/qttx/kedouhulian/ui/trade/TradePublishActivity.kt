package com.qttx.kedouhulian.ui.trade

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.trade.viewModel.TradePublishViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.BaseTextWatcher
import com.stay.toolslibrary.utils.BigDecimalUtils
import kotlinx.android.synthetic.main.trade_activity_publish.*
import org.koin.android.viewmodel.ext.android.viewModel


class TradePublishActivity : BaseActivity() {

    private val viewModel: TradePublishViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.trade_activity_publish

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("发布交易")
        taskAcountEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                val len = s.toString().length

                if (text.startsWith(".")) {
                    //起始为. 插入0成为0.
                    s.insert(0, "0")
                } else if (len >= 2 && text.startsWith("0") && !text.contains(".")) {
                    //如果起始是0但是没输入 点  则剔除掉0
                    s.replace(0, 1, "")
                } else if (text.contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 4) {
                        s.replace(s.length - 1, s.length, "")
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setPrice()
            }
        }
        )

        taskPriceEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                val len = s.toString().length

                if (text.startsWith(".")) {
                    //起始为. 插入0成为0.
                    s.insert(0, "0")
                } else if (len >= 2 && text.startsWith("0") && !text.contains(".")) {
                    //如果起始是0但是没输入 点  则剔除掉0
                    s.replace(0, 1, "")
                } else if (text.contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        s.replace(s.length - 1, s.length, "")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setPrice()
            }
        }

        )
        viewModel.getConfig(this)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getConfig(this)
    }

    override fun liveDataListener() {
        viewModel.buyLiveData.toObservable(this)
        {

            it.data?.let {

                if (it.paymoney > 0) {
                    PayActivity.newInstance(it)
                            .setListener(PayResult {
                                showToast("挂售成功")
                                setResult(400)
                                finish()
                            })
                            .show(supportFragmentManager)

                } else {
                    showToast("挂售成功")
                    setResult(400)
                    finish()
                }
            }
        }

        viewModel.configLiveData.toObservable(this)
        {
            it.data?.let {
                allPriceTv.text = "${it.kd_num}枚"
                isTopTv.text = "置顶 ${it.trade_top}元/次"
            }
        }
        initViewState()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            agreeTv -> {
                viewModel.isAgree = 1
                initViewState()
            }
            refuseTv -> {
                viewModel.isAgree = 0
                initViewState()
            }
            noTopTv -> {
                viewModel.isTop = 0
                initViewState()
                topPriceTv.text = ""
            }
            isTopTv -> {
                viewModel.isTop = 1
                initViewState()
                topPriceTv.text = viewModel.configLiveData.value?.data?.trade_top.toString()
            }
            publishTv -> {
                chekData()
            }
        }
    }

    private fun initViewState() {
        agreeTv.isSelected = viewModel.isAgree == 1
        refuseTv.isSelected = viewModel.isAgree == 0
        isTopTv.isSelected = viewModel.isTop == 1
        noTopTv.isSelected = viewModel.isTop == 0
    }

    override fun initViewClickListeners() {
        agreeTv.setOnClickListener(this)
        refuseTv.setOnClickListener(this)
        noTopTv.setOnClickListener(this)
        isTopTv.setOnClickListener(this)
        publishTv.setOnClickListener(this)
    }

    private fun setPrice() {
        viewModel.configLiveData
                .value?.data?.let {
            val account = taskAcountEt.text.toString()
            val taskPrice = taskPriceEt.text.toString()
            if (account.isEmpty() || taskPrice.isEmpty()) {
                taskPriceTv.text = "0"
            } else {
                val priceAll = BigDecimalUtils.mul(account, taskPrice, 4)
                taskPriceTv.text = priceAll
            }
        }
    }

    private fun chekData() {

        val account = taskAcountEt.text.toString()
        if (account.isEmpty()) {
            showToast("请输入出售数量")
            return
        }

        val taskPrice = taskPriceEt.text.toString()
        if (taskPrice.isEmpty()) {
            showToast("请输入出售单价")
            return
        }
        val map = mutableMapOf<String, String>()
        map["seller_num"] = account
        map["unit"] = taskPrice
        map["is_alter"] = viewModel.isAgree.toString()
        map["is_top"] = viewModel.isTop.toString()
        map["top_price"] = viewModel.configLiveData.value?.data?.trade_top.toString()
        viewModel.saveData(this, map)
    }
}
