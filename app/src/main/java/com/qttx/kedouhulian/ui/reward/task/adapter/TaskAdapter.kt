package com.qttx.kedouhulian.ui.reward.task.adapter

import android.content.Intent
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TaskBean
import com.qttx.kedouhulian.ui.reward.task.TaskHomeDetailActivity
import com.qttx.kedouhulian.ui.reward.task.TaskOrderDetailActivity
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.widget.ImageGrideShow
import kotlinx.android.synthetic.main.reward_list_item_task_grap.*
import java.lang.StringBuilder

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class TaskAdapter constructor(result: MutableList<TaskBean>) : RecyclerAdapter<TaskBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_task_grap


    override fun RecyclerViewHolder.bindData(item: TaskBean, position: Int) {
        with(item)
        {
            priceDetail.text = "[完成可获得$score 个蝌蚪币]"
            userAvatarIv.loadCircleAvatar(avatar)
            userName.text = nickname
            priceTv.text = task_price
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
            } else if (show_img == 1||uid== getUserId()) {

                //是不是抢了
                imageShowLayout.visibility = View.VISIBLE
                imageShowLayout.setListener(object : ImageGrideShow.OnTouchBlankPositionListener {
                    override fun onTouchBlank() {
                        val intent = Intent(mContext, TaskHomeDetailActivity::class.java)
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
            hotTv.text = heat.toString()
//            grapTv.setOnClickListener {
//            }


        }
    }


}
