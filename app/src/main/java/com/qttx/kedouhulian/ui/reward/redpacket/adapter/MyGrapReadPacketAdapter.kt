package com.qttx.kedouhulian.ui.reward.redpacket.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RedPacketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.reward_list_item__my_redpacket_grap.*
import java.lang.StringBuilder

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class MyGrapReadPacketAdapter constructor(result: MutableList<RedPacketBean>) : RecyclerAdapter<RedPacketBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item__my_redpacket_grap


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
            timeTv.text = create_time
            totalAcountTv.text = red_price
            stateTv.text = status
            socreTv.text = "获得$score 个蝌蚪币"

        }
    }


}
