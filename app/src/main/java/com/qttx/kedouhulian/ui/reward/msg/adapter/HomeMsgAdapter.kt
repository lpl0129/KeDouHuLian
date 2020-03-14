package com.qttx.kedouhulian.ui.reward.msg.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.HomeMsgBean
import com.qttx.kedouhulian.bean.RedPacketBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.home_list_item_msg.*

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class HomeMsgAdapter constructor(result: MutableList<HomeMsgBean>) : RecyclerAdapter<HomeMsgBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.home_list_item_msg


    override fun RecyclerViewHolder.bindData(item: HomeMsgBean, position: Int) {
        if (position == 0) {
            spiteIv.setImageResource(R.drawable.fabuxuanze_btn)
            spiteViewTop.visibility = View.INVISIBLE
        } else {
            spiteIv.setImageResource(R.drawable.fabuweixuan_btn)
            spiteViewTop.visibility = View.VISIBLE
        }
        with(item)
        {
            timeTv.text = "时间：${create_time}"
            contentTv.text = content
        }
    }


}
