package com.qttx.kedouhulian.ui.reward.buunty.adapter

import android.content.Intent
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.BountyBean
import com.qttx.kedouhulian.ui.reward.buunty.BountyDetailActivity
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.widget.ImageGrideShow
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.*
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.grapTv
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.titleTv
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.userAvatarIv
import kotlinx.android.synthetic.main.reward_list_item_grap_bounty.userName
import kotlinx.android.synthetic.main.reward_list_item_redpacket_grap.*
import java.lang.StringBuilder

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class BountyAdapter constructor(result: MutableList<BountyBean>) : RecyclerAdapter<BountyBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_grap_bounty


    override fun RecyclerViewHolder.bindData(item: BountyBean, position: Int) {
        with(item)
        {
            des_tv.text= "$price/$unit"
            userAvatarIv.loadCircleAvatar(avatar)
            userName.text = nickname
            locationTv.text = area_name
            val builder = StringBuilder()
            if (is_top == 1) {
                builder.append("[顶] ")
            }
            builder.append("[$type_name] ")
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()

            if (imgs_list.isEmpty()) {
                imageHideTv.visibility = View.GONE
                imageShowLayout.visibility = View.GONE
            } else if (hidden_img == 0||uid== getUserId()||"1"==current_status) {
                imageShowLayout.visibility = View.VISIBLE
                imageShowLayout.setListener(object : ImageGrideShow.OnTouchBlankPositionListener {
                    override fun onTouchBlank() {
                        val intent = Intent(mContext, BountyDetailActivity::class.java)
                        intent.putExtra("id", item.id.toString())
                        mContext.startActivity(intent)
                    }
                }
                )
                imageHideTv.visibility = View.GONE
                imageShowLayout.setImageList(imgs_list)
            } else {
                imageShowLayout.visibility = View.GONE
                imageHideTv.visibility = View.VISIBLE
            }
            guyongTv.text=employ_num.toString()
            baomingTv.text=submit_num.toString()
            countTv.text=total_num.toString()

            if ("1"==current_status)
            {
                grapTv.setBackgroundResource(R.drawable.e0e0_bk_15)
                grapTv.setTextColor(mContext.resources.getColor(R.color.whiteColor))
                grapTv.text="已报名"

            }else
            {
                grapTv.setBackgroundResource(R.drawable.tag_15_line)
                grapTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                grapTv.text="报名 / 抢单"
            }
        }
    }


}
