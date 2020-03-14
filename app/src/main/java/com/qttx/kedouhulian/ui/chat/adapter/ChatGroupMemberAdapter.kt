package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.amap.api.mapcore.util.fa
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.FriendBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.chat_list_item_group_memer.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class ChatGroupMemberAdapter constructor(result: MutableList<FriendBean>) : RecyclerAdapter<FriendBean>(result) {

    var isOwner = false

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_group_memer
    }

    override fun RecyclerViewHolder.bindData(item: FriendBean, position: Int) {
        with(item)
        {
            userAvatarIv.loadCircleAvatar(avatarUrl)
            userNameTv.text = mark
            addOnItemChildClickListener(agreeTv, position = position)
            if (is_grouper == 1 || isOwner) {
                agreeTv.visibility = View.GONE
            } else {
                agreeTv.visibility = View.VISIBLE
            }
            addOnItemChildClickListener(agreeTv, position = position)
        }
    }

}