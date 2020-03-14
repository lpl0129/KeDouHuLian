package com.qttx.kedouhulian.ui.reward.redpacket.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RedPacketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.SpanUtils
import kotlinx.android.synthetic.main.reward_list_item_my_grap_task.*
import kotlinx.android.synthetic.main.reward_list_item_my_publish_red_packet.*
import kotlinx.android.synthetic.main.reward_list_item_my_publish_red_packet.priceTv
import kotlinx.android.synthetic.main.reward_list_item_my_publish_red_packet.timeDay
import kotlinx.android.synthetic.main.reward_list_item_my_publish_red_packet.timeMonth
import kotlinx.android.synthetic.main.reward_list_item_my_publish_red_packet.titleTv
import java.lang.StringBuilder
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishRedPacketAdapter  constructor(result: MutableList<RedPacketBean>) : RecyclerAdapter<RedPacketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_my_publish_red_packet


    override fun RecyclerViewHolder.bindData(item: RedPacketBean, position: Int) {
        with(item)
        {

            //                        状态:-3=已撤销,-2=暂停,-1=待支付,,1=已完成.0=进行中
            when (status.toInt()) {
                0 -> {
                    state_tv.text="进行中"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -2 -> {
                    state_tv.text="暂停中"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                -1 -> {
                    state_tv.text="待支付"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                -3 -> {
                    state_tv.text="已结算"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
                1 -> {
                    state_tv.text="已完成"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
            }

            timeTv.text=DateUtils.date2String(Date(item.create_time.toLong()*1000))
            val builder = StringBuilder()
            if (is_top == 1) {
                builder.append("[顶] ")
            }
            builder.append("[$red_type] ")
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()

            timeMonth.text = " /$month 月"
            timeDay.text = day
            priceTv.text = "¥$total_price"
            totalAcountTv.text=total_num.toString()
            leftTv.text="${sycount}/${sy_price}"

        }
    }
}