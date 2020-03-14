package com.qttx.kedouhulian.ui.reward.redpacket.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RedPacketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.*
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.*
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.grapTv
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.titleTv
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.userAvatarIv
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.userName
import java.lang.StringBuilder

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class ReadPacketAdapter constructor(result: MutableList<RedPacketBean>) : RecyclerAdapter<RedPacketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_redpacket_grap


    override fun RecyclerViewHolder.bindData(item: RedPacketBean, position: Int) {
        with(item)
        {
            userAvatarIv.loadCircleAvatar(avatar)
            userName.text = username
            priceTv.text = "¥$total_price"
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

            timeTv.text=create_time_text

            hotTv.text = ylcount.toString()

            totalAcountTv.text=total_num.toString()
            leftTv.text=sy_price

            if ("1"==current_status)
            {
                grapTv.setBackgroundResource(R.drawable.e0e0_bk_15)
                grapTv.setTextColor(mContext.resources.getColor(R.color.whiteColor))
                grapTv.text="已领取"
            }else
            {
                grapTv.setBackgroundResource(R.drawable.tag_15_line)
                grapTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                grapTv.text="抢红包"
            }
        }
    }


}
