package com.qttx.kedouhulian.ui.trade

import android.os.Bundle
import android.text.Html
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.trade.viewModel.TradeDetailViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.trade_activity__mark_detail.*

import org.koin.android.viewmodel.ext.android.viewModel


class TradeDetailActivity : BaseActivity() {

    private val viewModel: TradeDetailViewModel by viewModel()

    private var id: String = ""

    override fun getLayoutId(): Int = R.layout.trade_activity__mark_detail

    override fun liveDataListener() {
        viewModel.descriptionLiveData.toObservable(this)
        {
            it.data?.let {
                descriptionTv.text = Html.fromHtml(it.content)
            }
        }
        viewModel.detailLiveData.toObservable(this)
        {
            it.data?.let {
                with(it)
                {
                    nameTv.text = username
                    buyPriceTv.text = "买入蝌蚪币单价：¥${unit}元"

                    buyNumberTv.text = "买入蝌蚪币数量：¥${seller_num}枚"

                    allPriceTv.text = "¥$amount"
                    if (uid.toString() == getUserId()) {
                        chartTv.visibility = View.GONE
                    } else {
                        chartTv.visibility = View.VISIBLE
                        if (is_alter == 1) {
                            //接受洽谈
                            chartTv.setBackgroundResource(R.drawable.primary_10_bk)
                            chartTv.isClickable = true
                        } else {
                            chartTv.setBackgroundResource(R.drawable.spite_10_bk)
                            chartTv.isClickable = false
                        }
                    }

                }
            }
        }
        viewModel.buyLiveData.toObservable(this)
        {
            it.data?.let {
                PayActivity.newInstance(it)
                        .setListener(PayResult {
                            setResult(400)
                            finish()
                        })
                        .show(supportFragmentManager)
            }
        }
        viewModel.buyPayLiveData.toObservable(this)
        {
            it.data?.let {
               showToast("已加入")
            }
        }
    }

    override fun initViewClickListeners() {
        chartTv.setOnClickListener {
            viewModel.detailLiveData
                    .value?.data?.let {
                jumpToChat(this, it.uid.toString(), it.username, it.avatar)
            }
        }
        submitTv.setOnClickListener {
            viewModel.buyData(this)
        }
        cancleTv.setOnClickListener {
            viewModel.buyDataPay(this)
        }
        submit_pay.setOnClickListener {
            viewModel.buyDataPay(this)
        }


    }

    override fun processLogic(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        setTopTitle("详情")
        viewModel.getData(this, id)
        viewModel.descriptionData(this)
    }


}
