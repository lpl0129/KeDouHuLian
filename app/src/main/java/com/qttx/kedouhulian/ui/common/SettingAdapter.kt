package com.qttx.kedouhulian.ui.common

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SystemNotifyCateBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.common_list_item_setting.*

/**
 * @author huangyr
 * @date 2019/5/21 0021
 */
class SettingAdapter constructor(list:MutableList<SystemNotifyCateBean>) :RecyclerAdapter<SystemNotifyCateBean>(list)
{
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.common_list_item_setting
    }

    override fun RecyclerViewHolder.bindData(item: SystemNotifyCateBean, position: Int) {
        titleTv.text=item.name

        if (item.value==1) {
            notifyIv.setImageResource(R.drawable.dakai_btn)

        } else {
            notifyIv.setImageResource(R.drawable.butongzhi_btn)
        }
    }

}