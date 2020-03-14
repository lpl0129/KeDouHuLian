package com.qttx.kedouhulian.ui.trade.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TradeMarketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import kotlinx.android.synthetic.main.trade_list_item_my_publish_ing.*
import java.lang.StringBuilder

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishTradeIngAdapter constructor(result: MutableList<TradeMarketBean>) : RecyclerAdapter<TradeMarketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.trade_list_item_my_publish_ing

    override fun RecyclerViewHolder.bindData(item: TradeMarketBean, position: Int) {
        with(item)
        {
            val builder = StringBuilder()
            if (is_top==1)
            {
                builder.append("[顶] ")
            }
            viewNumberTv.text="${view}次"

            timeDay.text=create_time.split("/")[0]
            timeMonth.text="/ ${create_time.split("/")[1]}"

            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append("出售数量: ${seller_num}枚")
                    .create()

            priceTv.text="出售价：${unit}元／枚 总价合计：${amount}元"
            cancleTv.visibility=View.GONE
            addOnItemChildClickListener(editPriceTv, cancleTv, position = position)
//            状态 -2 已撤销 -1 待支付 0 挂售中 1 已完成
            when (status) {
                0 -> {
                    statusTv.text = "挂售中"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                    cancleTv.visibility=View.VISIBLE
                }
                1 -> {
                    statusTv.text = "已完成"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }

                -1 -> {
                    statusTv.text = "待支付"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                -2 -> {
                    statusTv.text = "已撤销"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
//
            }


        }
    }
}