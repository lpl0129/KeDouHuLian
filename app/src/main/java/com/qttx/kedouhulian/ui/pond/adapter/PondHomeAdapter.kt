package com.qttx.kedouhulian.ui.pond.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PondBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.pond_list_item_home.*

/**
 * @author huangyr
 * @date 2019/5/6 0006
 */
class PondHomeAdapter constructor(result: MutableList<PondBean>) : RecyclerAdapter<PondBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.pond_list_item_home
    }

    override fun RecyclerViewHolder.bindData(item: PondBean, position: Int) {
        with(item)
        {
            nickerNameTv.text=nickname
            userAvatarIv.loadCircleAvatar(avatar)
            incomeTv.text = income
            buyPriceTv.text = "¥$buy_price"
            nowPriceTv.text = "¥$sell_price"
            ratingBar.rating = star.toFloat()
            locationTv.text = pca
        }
        addOnItemChildClickListener(buyTv, position = position)
    }

}