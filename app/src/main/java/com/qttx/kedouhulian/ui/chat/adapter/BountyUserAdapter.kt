package com.qttx.kedouhulian.ui.chat.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.BountyUserBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.chat_list_item_reward.*

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
class BountyUserAdapter constructor(result: MutableList<BountyUserBean>) : RecyclerAdapter<BountyUserBean>(result) {

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.chat_list_item_reward
    }

    override fun RecyclerViewHolder.bindData(item: BountyUserBean, position: Int) {
        with(item)
        {
            addOnItemChildClickListener(jieguTv, guyongTv, jiesuanTv, userLl, shensuTv, position = position)
            if (uid.isEmpty()) {
                managerLL.visibility = View.INVISIBLE
                imageIv.setImageResource(R.drawable.chakanquanbu_btn)
                textTv.text = "查看全部"

            } else {
                imageIv.loadCircleAvatar(avatar)
                textTv.text = nickname
                managerLL.visibility = View.VISIBLE
                jieguTv.visibility = View.GONE
                guyongTv.visibility = View.GONE
                shensuTv.visibility = View.GONE
                jiesuanTv.visibility = View.GONE
                stateTv.visibility = View.GONE
//                状态:-2=已解雇,-1=解雇中,0=已报名,1=已雇佣,2=已结算
//            是否申请结算 0：没有申请 1：已申请
                when (status) {
                    -2 -> {
                        stateTv.visibility = View.VISIBLE
                        stateTv.text = "已解雇"
                    }
                    -1 -> {
                        stateTv.visibility = View.VISIBLE
                        if (agree == -1) {
                            //
                            stateTv.text = "被拒绝"
                            shensuTv.visibility = View.VISIBLE
                        } else if (agree == -2) {
                            stateTv.text = "已申诉"
                        } else {
                            stateTv.text = "解雇中"
                        }
                    }
                    0 -> {
                        guyongTv.visibility = View.VISIBLE
                        jiesuanTv.visibility = View.VISIBLE
                        jiesuanTv.isEnabled = false
                        jiesuanTv.setBackgroundResource(R.drawable.b2_5_bk)
                    }
                    1 -> {
                        jieguTv.visibility = View.VISIBLE
                        jiesuanTv.visibility = View.VISIBLE
                        jiesuanTv.isEnabled = true
                        if (is_apply_finish == 0) {
                            jiesuanTv.setBackgroundResource(R.drawable.fb_5_bk)
                        } else {
                            jiesuanTv.setBackgroundResource(R.drawable.tag_bk_5)
                        }
                    }
                    2 -> {
                        stateTv.visibility = View.VISIBLE
                        stateTv.text = "已结算"
                    }
                }
            }


        }
    }

}