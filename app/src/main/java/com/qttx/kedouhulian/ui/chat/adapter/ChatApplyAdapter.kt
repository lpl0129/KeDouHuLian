package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.ChatApplyBean
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
class ChatApplyAdapter constructor(result: MutableList<ChatApplyBean>) : RecyclerAdapter<ChatApplyBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_search
    }

    override fun RecyclerViewHolder.bindData(item: ChatApplyBean, position: Int) {
        with(item)
        {

            addOnItemChildClickListener(agreeTv, position = position)
            userAvatarIv.loadCircleAvatar(avatar)
            userNameTv.text = username
            noteTv.visibility=View.VISIBLE
            noteTv.text=content
            when(status)
            {
                -1->{

                }
                0->{
                    agreeTv.visibility = View.VISIBLE
                    agreeTv.text = "同意"
                    statusTv.visibility = View.GONE
                }
                1->{
                    agreeTv.visibility = View.GONE
                    statusTv.visibility = View.VISIBLE
                    statusTv.text = "已添加"
                }
            }
        }
    }

}