package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.FriendBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.chat_list_item_contact.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class ChatContactAdapter constructor(result: MutableList<FriendBean>) : RecyclerAdapter<FriendBean>(result) {

    var type = 0

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_contact
    }

    override fun RecyclerViewHolder.bindData(item: FriendBean, position: Int) {
        with(item)
        {

            if (position == 0) {
                titleTv.visibility = View.VISIBLE
            } else {
                val secondString = mItems[position - 1].pyname
                if (item.pyname == secondString) {
                    titleTv.visibility = View.GONE
                } else {
                    titleTv.visibility = View.VISIBLE
                }
            }
            titleTv.text = pyname
            userAvatarIv.loadCircleAvatar(avatarUrl)
            userNameTv.text = mark

            if (type == 0) {
                if (item.canSelect) {
                    if (item.select) {
                        checkbox.setImageResource(R.drawable.zhifuxuanze_btn)
                    } else {
                        checkbox.setImageResource(R.drawable.zhifuweixuan_btn)
                    }
                } else {
                    checkbox.setImageResource(R.drawable.fabuweixuan_btn)
                }
            } else {
                checkbox.visibility = View.GONE
            }

        }
    }

//    fun checkCanSelect(item: FriendBean): Boolean {
//        return userinfo.none {
//            it.uid == item.uid
//        }
//    }

}