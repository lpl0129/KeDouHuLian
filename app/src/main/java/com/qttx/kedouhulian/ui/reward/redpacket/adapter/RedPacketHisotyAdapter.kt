package com.qttx.kedouhulian.ui.reward.redpacket.adapter

import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RedPacketHistory
import com.qttx.kedouhulian.bean.TaskDetailBean
import com.qttx.kedouhulian.bean.TaskMsgBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.reward_list_item_task_commend.*

/**
 * @author huangyr
 * @date 2019/4/17 0017
 */
class RedPacketHisotyAdapter constructor(list: MutableList<RedPacketHistory>) : RecyclerAdapter<RedPacketHistory>(list) {


    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.reward_list_item_redpacket_history
    }

    override fun RecyclerViewHolder.bindData(item: RedPacketHistory, position: Int) {
        with(item)
        {
            nameTv.text = username
            avatar_iv.loadCircleAvatar(avatar)
            contentTv.text ="${red_price}元"
            timeTv.text = create_time
        }

    }

}