package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SearchBean
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.chat_list_item_search.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class ChatSearchAdapter constructor(result: MutableList<SearchBean>) : RecyclerAdapter<SearchBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_search
    }

    override fun RecyclerViewHolder.bindData(item: SearchBean, position: Int) {
        with(item)
        {

            addOnItemChildClickListener(agreeTv, position = position)
            userAvatarIv.loadCircleAvatar(avatar)
            userNameTv.text = name
            if (item.type == 1) {
                if (is_group == 1) {
                    //在群中
                    agreeTv.visibility = View.GONE
                    statusTv.visibility = View.VISIBLE
                    statusTv.text = "已加入"
                } else {
                    agreeTv.visibility = View.VISIBLE
                    agreeTv.text = "申请加群"
                    statusTv.visibility = View.GONE

                }
            } else {
                if (getUserId() == uid.toString()) {
                    //自己
                    statusTv.visibility = View.GONE
                    agreeTv.visibility = View.GONE
                } else if (is_friend == 1) {
                        statusTv.visibility = View.VISIBLE
                        agreeTv.visibility = View.GONE

                        statusTv.text = "已添加"
                    } else {
                        agreeTv.visibility = View.VISIBLE
                        agreeTv.text = "添加好友"
                        statusTv.visibility = View.GONE

                    }

            }
        }
    }

}