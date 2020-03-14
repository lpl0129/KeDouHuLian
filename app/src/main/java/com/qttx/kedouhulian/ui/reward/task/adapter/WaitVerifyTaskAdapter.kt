package com.qttx.kedouhulian.ui.reward.task.adapter

import android.graphics.Color
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TaskBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.reward_list_item_wait_verify_task.*

/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
class WaitVerifyTaskAdapter(mItems: MutableList<TaskBean>) : RecyclerAdapter<TaskBean>(mItems) {

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.reward_list_item_wait_verify_task
    }

    override fun RecyclerViewHolder.bindData(item: TaskBean, position: Int) {
        with(item)
        {
            userAvatarIv.loadCircleAvatar(avatar)
            userName.text = nickname
            titleTv.text=content
            imageShowLayout.setImageList(imgs_list)
            taskTimeTv.text = create_time_text
            when (status) {
                1 -> {
                    if (amend_status == 0) {
                        //待审核
                        //已通过
                        taskState.text = "待审核"
                        taskState.setTextColor(mContext.resources.getColor(R.color.primaryColor))

                    } else {
                        //待修改
                        taskState.text = "待审核"
                        taskState.setTextColor(Color.parseColor("#FFFB861D"))
                    }
                    rightArrowIV.visibility = View.VISIBLE
                }
                2 -> {
                    //已通过
                    taskState.text = "已通过"
                    taskState.setTextColor(mContext.resources.getColor(R.color.tintColor))
                    rightArrowIV.visibility = View.GONE
                }
                -1 -> {
                    //不合格
                    taskState.text = "不合格"
                    taskState.setTextColor(mContext.resources.getColor(R.color.tintColor))
                    rightArrowIV.visibility = View.GONE
                }
            }
            addOnItemChildClickListener(taskState, position = position)

        }
    }

}
