package com.qttx.kedouhulian.ui.pond.adapter

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PondBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.pond_list_item_mine.*

/**
 * @author huangyr
 * @date 2019/5/6 0006
 */
class MyPondAdapter constructor(result: MutableList<PondBean>) : RecyclerAdapter<PondBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.pond_list_item_mine
    }

    override fun RecyclerViewHolder.bindData(item: PondBean, position: Int) {
        with(item)
        {

            locationTv.text = pool_name

            timeTv.text = create_time

            buyPriceTv.text = "¥$buy_price 元"

            incomeZuJinTv.text = "¥$profit 元"

            zhuanRangTv.text = if (type == 0) "定价转让" else "取消转让"
        }
        addOnItemChildClickListener(zhuanRangTv, position = position)
    }

}