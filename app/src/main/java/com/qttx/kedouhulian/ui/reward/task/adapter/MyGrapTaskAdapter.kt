package com.qttx.kedouhulian.ui.reward.task.adapter

import android.graphics.Color

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.MyGrapTaskBean

import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder

import kotlinx.android.synthetic.main.reward_list_item_my_grap_task.*


/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
class MyGrapTaskAdapter(mItems: MutableList<MyGrapTaskBean>) : RecyclerAdapter<MyGrapTaskBean>(mItems) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_my_grap_task


    override fun RecyclerViewHolder.bindData(item: MyGrapTaskBean, position: Int) {
        with(item)
        {
            titleTv.text = title
            timeMonth.text = " /$month 月"
            timeDay.text = day
            taskNum.text = "任务编号: $number"
            priceTv.text = task_price

            priceTv.text = task_price

            when (status) {
                0 -> {
                    taskRateTv.text = "待提交"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                    socreTv.text = "完成后将会获得$score 个蝌蚪币"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                1 -> {
                    taskRateTv.text = "待审核"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                    socreTv.text = "完成后将会获得$score 个蝌蚪币"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))

                }
                2 -> {
                    taskRateTv.text = "已完成"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                    socreTv.text = "获得$score  个蝌蚪币已入账"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                3 -> {
                    taskRateTv.text = "待修改"
                    taskRateTv.setTextColor(Color.parseColor("#FFFB861D"))
                    socreTv.text = "完成后将会获得$score 个蝌蚪币"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -1 -> {
                    taskRateTv.text = "不合格"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                    socreTv.text = "如果对拒绝理由不满意,可进行投诉"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -2 -> {
                    taskRateTv.text = "已申诉"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                    socreTv.text = "完成后将会获得$score  个蝌蚪币"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                -3 -> {
                    taskRateTv.text = "已过期"
                    taskRateTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                    socreTv.text = "任务已过期，无法获得蝌蚪币"
                    socreTv.setTextColor(mContext.resources.getColor(R.color.tintColor))
                }
            }

        }
    }
}

