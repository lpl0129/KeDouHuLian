package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.GroupListBean
import com.qttx.kedouhulian.bean.SearchBean
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import io.rong.imlib.statistics.UserData.username
import kotlinx.android.synthetic.main.chat_list_item_search.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class ChatGroupAdapter constructor(result: MutableList<GroupListBean>) : RecyclerAdapter<GroupListBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_search
    }

    override fun RecyclerViewHolder.bindData(item: GroupListBean, position: Int) {
        with(item)
        {

            userAvatarIv.loadCircleAvatar(group_avatar)
            userNameTv.text = group_name
            noteTv.visibility=View.GONE
            agreeTv.visibility=View.GONE

        }
    }

}