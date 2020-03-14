package com.qttx.kedouhulian.ui.trade.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TradeMarketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.trade_list_item_my_publish_end.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishTradeEndAdapter constructor(result: MutableList<TradeMarketBean>) : RecyclerAdapter<TradeMarketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.trade_list_item_my_publish_end

    override fun RecyclerViewHolder.bindData(item: TradeMarketBean, position: Int) {
        with(item)
        {

            numberTv.text = "卖出数量：${buy_num}枚"
            sellNameTv.text = "买入者：$buyer_name"
            sellPriceTv.text = "出售价：${unit}元／枚 "
            selTimeTv.text = "卖出时间：$create_time"
//            状态 -2 已撤销 -1 待支付 0 挂售中 1 已完成
            when (status) {
                0 -> {
                    statusTv.text = "挂售中"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                1 -> {
                    statusTv.text = "已完成"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
                -1 -> {
                    statusTv.text = "待付款"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
                -2 -> {
                    statusTv.text = "已撤销"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
            }
        }
    }
}