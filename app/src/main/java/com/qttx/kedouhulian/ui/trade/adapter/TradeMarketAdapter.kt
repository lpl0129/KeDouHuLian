package com.qttx.kedouhulian.ui.trade.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TradeMarketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.trade_list_item_market.*

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class TradeMarketAdapter constructor(result: MutableList<TradeMarketBean>) : RecyclerAdapter<TradeMarketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.trade_list_item_market


    override fun RecyclerViewHolder.bindData(item: TradeMarketBean, position: Int) {
        with(item)
        {
            nameTv.text = username
            numberTv.text = seller_num
            priceTv.text = unit
            statusTv.visibility = View.VISIBLE
            buyTv.visibility = View.GONE

//            when (status) {
//                -2 -> {
//                    statusTv.text = "已撤销"
//                }
//                -1 -> {
//                    statusTv.text = "待支付"
//                }
//                -0 -> {
                    buyTv.visibility = View.VISIBLE
//                    statusTv.visibility = View.GONE
//                }
//                1 -> {
//                    statusTv.text = "已完成"
//                }
//            }
        }
    }


}
