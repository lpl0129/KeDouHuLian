package com.qttx.kedouhulian.ui.wallet.adapter

import android.graphics.Color
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.WalletListBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import kotlinx.android.synthetic.main.wallet_list_item_aeest.*

/**
 * @author huangyr
 * @date 2019/5/17 0017
 */
class WallAesstListAdapter constructor(result: MutableList<WalletListBean>) : RecyclerAdapter<WalletListBean>(result) {
    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.wallet_list_item_aeest
    }

    override fun RecyclerViewHolder.bindData(item: WalletListBean, position: Int) {

        with(item)
        {
            titleTv.text = memo
            timeTv.text = create_time_text
            if (money.isNotEmpty())
            {
                numberTv.text = "${money}元"
                if (money.contains("-")) {
                    numberTv.setTextColor(Color.parseColor("#FF333333"))
                } else {
                    numberTv.setTextColor(Color.parseColor("#FFFE5555"))
                }
            }else
            {
                numberTv.text = "${score}"
                if (score.contains("-")) {
                    numberTv.setTextColor(Color.parseColor("#FF333333"))
                } else {
                    numberTv.setTextColor(Color.parseColor("#FFFE5555"))
                }
            }


        }
    }


}