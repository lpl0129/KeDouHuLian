package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.ChatCustomMsgBean
import com.qttx.kedouhulian.bean.SearchBean
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.chat_list_item_custom_msg.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class ChatCustomMsgAdapter constructor(result: MutableList<ChatCustomMsgBean>) : RecyclerAdapter<ChatCustomMsgBean>(result) {

    var isEdit = false

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_custom_msg
    }

    override fun RecyclerViewHolder.bindData(item: ChatCustomMsgBean, position: Int) {
        textTv.text = item.content
        if (isEdit) {
            delIv.visibility = View.VISIBLE
        } else {
            delIv.visibility = View.GONE
        }
        addOnItemChildClickListener(delIv, position = position)
    }

}