package com.qttx.kedouhulian.ui.user.adapter

import android.graphics.Color
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.CopartnerBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.user_list_item_copartner.*
import kotlinx.android.synthetic.main.wallet_list_item_aeest.*
import kotlinx.android.synthetic.main.wallet_list_item_aeest.timeTv

/**
 * @author huangyr
 * @date 2019/5/17 0017
 */
class CopartnerAdapter constructor(result: MutableList<CopartnerBean>) : RecyclerAdapter<CopartnerBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.user_list_item_copartner
    }

    override fun RecyclerViewHolder.bindData(item: CopartnerBean, position: Int) {

        with(item)
        {
            nameTv.text = username
            timeTv.text = jointime
            incomeTv.text = accumulated_earnings

        }
    }


}