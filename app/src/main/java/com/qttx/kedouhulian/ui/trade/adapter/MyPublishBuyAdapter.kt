package com.qttx.kedouhulian.ui.trade.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TradeMyBuy
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.trade_list_item_my_buy.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishBuyAdapter constructor(result: MutableList<TradeMyBuy>) : RecyclerAdapter<TradeMyBuy>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.trade_list_item_my_buy

    override fun RecyclerViewHolder.bindData(item: TradeMyBuy, position: Int) {
        with(item)
        {
            addOnItemChildClickListener(cancletv,pay_tv,position = position)
            chartTv.visibility=View.GONE
            when (status) {
                -1 -> {
                    statusTv.text = "已撤销"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
                1 -> {
                    statusTv.text = "已完成"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
                -0 -> {
                    chartTv.visibility=View.VISIBLE
                    chartTv.setOnClickListener {
                        jumpToChat(itemView.context, seller_id,seller_name,avatar)
                    }
                    statusTv.text = "待付款"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                    managerLl.visibility=View.VISIBLE
                }

            }
            if (is_alter == 1) {
                //接受洽谈
                chartTv.setBackgroundResource(R.drawable.primary_10_bk)
                chartTv.isClickable = true
            } else {
                chartTv.setBackgroundResource(R.drawable.spite_10_bk)
                chartTv.isClickable = false
            }
            timeDay.text=ctime.split("/")[0]
            timeMonth.text="/ ${ctime.split("/")[1]}"
            numberTv.text = "买入数量：${buy_num}枚"

            sellNameTv.text = "购入花费：${amount}元"
            sellPriceTv.text = "购入价：${unit}元／枚 "
            publishUserNameTv.text = seller_name
//            状态 -2 已撤销 -1 待支付 0 挂售中 1 已完成

        }
    }
}