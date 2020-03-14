package com.qttx.kedouhulian.ui.pond.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PondHistoryBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.pond_list_item_charge_history.*

/**
 * @author huangyr
 * @date 2019/4/17 0017
 */
class PondHisotyAdapter constructor(list: MutableList<PondHistoryBean>) : RecyclerAdapter<PondHistoryBean>(list) {


    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.pond_list_item_charge_history
    }

    override fun RecyclerViewHolder.bindData(item: PondHistoryBean, position: Int) {
        with(item)
        {
            nameTv.text = nickname
            avatar_iv.loadCircleAvatar(avatar)
            timeTv.text = create_time
        }

    }

}