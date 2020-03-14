package com.qttx.kedouhulian.ui.reward.task.adapter

import android.app.Activity
import android.content.Intent
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TaskBean
import com.qttx.kedouhulian.ui.reward.task.MyTaskDeatilActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.widget.ImageGrideShow
import kotlinx.android.synthetic.main.reward_list_item_my_publish_task.*
import java.lang.StringBuilder
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishAdapter constructor(result: MutableList<TaskBean>) : RecyclerAdapter<TaskBean>(result) {



    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_my_publish_task


    override fun RecyclerViewHolder.bindData(item: TaskBean, position: Int) {
        with(item)
        {
            val builder = StringBuilder()
            if (is_top == 1) {
                builder.append("[顶] ")
            }
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()
            imageShowLayout.setListener(object : ImageGrideShow.OnTouchBlankPositionListener {
                override fun onTouchBlank() {

                    val acitivy = mContext as Activity
                    val intent = Intent(mContext, MyTaskDeatilActivity::class.java)
                    intent.putExtra("id", item.id)
                    acitivy. startActivityForResult(intent, 200)
                }
            }
            )
            imageShowLayout.setImageList(imgs_list)

            val cal = Calendar.getInstance()
            cal.time = Date(create_time * 1000)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)

            timeMonth.text = "/ $month 月"
            timeDay.text = day.toString()
            locationTv.text = area

            dsnumTv.text = dsnum.toString()
            rateTv.text = rate.toString()
//            -2 已撤销 -1 待支付 0 进行中 1 已完成
            when (status) {
                0 -> {
                    state_tv.text="进行中"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -2 -> {
                    state_tv.text="已结算"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
                -1 -> {
                    state_tv.text="待支付"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }
                1 -> {
                    state_tv.text="已完成"
                    state_tv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
            }
        }
    }
}