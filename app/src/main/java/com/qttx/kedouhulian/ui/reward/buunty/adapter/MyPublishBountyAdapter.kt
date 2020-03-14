package com.qttx.kedouhulian.ui.reward.buunty.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.BountyBean
import com.qttx.kedouhulian.bean.RedPacketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import kotlinx.android.synthetic.main.reward_list_item_my_grap_bounty.*
import java.lang.StringBuilder
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishBountyAdapter  constructor(result: MutableList<BountyBean>) : RecyclerAdapter<BountyBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_my_publish_bounty


    override fun RecyclerViewHolder.bindData(item: BountyBean, position: Int) {
        with(item)
        {
            val builder = StringBuilder()
            builder.append("[$type_name] ")
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()
            locationTv.text=area_name
            val cal = Calendar.getInstance()
            cal.time = Date(create_time * 1000)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            timeMonth.text = "/ $month 月"
            timeDay.text = day.toString()
//            socreTv.text = "工作完成后将会获得$seller_score 个蝌蚪币"
            priceTv.text = "$price"
            priceUnitTv.text="/$unit"
            numberAcountTv.text=total_num.toString()

            when (status) {
                0 -> {
                    socreTv.text="进行中"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -2 -> {
                    socreTv.text="暂停中"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                -1 -> {
                    socreTv.text="待支付"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                -3 -> {
                    socreTv.text="已结算"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
                1 -> {
                    socreTv.text="已完成"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
            }

        }
    }
}