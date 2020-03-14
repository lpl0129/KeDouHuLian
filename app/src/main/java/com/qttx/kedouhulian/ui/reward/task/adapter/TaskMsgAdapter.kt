package com.qttx.kedouhulian.ui.reward.task.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TaskDetailBean
import com.qttx.kedouhulian.bean.TaskMsgBean
import com.qttx.kedouhulian.ui.reward.task.TaskCommentActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.library.nestfulllistview.NestFullListView
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import kotlinx.android.synthetic.main.reward_list_item_task_commend.*

/**
 * @author huangyr
 * @date 2019/4/17 0017
 */
class TaskMsgAdapter constructor(list: MutableList<TaskMsgBean>) : RecyclerAdapter<TaskMsgBean>(list) {

    public var isbounty = false

    override fun getLayoutIdByType(viewType: Int): Int {
        return R.layout.reward_list_item_task_commend
    }

    override fun RecyclerViewHolder.bindData(item: TaskMsgBean, position: Int) {
        with(item)
        {

            replayTv.setOnClickListener {
                val conttext = mContext
                val activity: Activity = conttext as Activity
                val intent = Intent(activity, TaskCommentActivity::class.java)
                intent.putExtra("id", item.id.toString())
                intent.putExtra("bounty", isbounty)
                intent.putExtra("b_id", "0")
                activity.startActivityForResult(intent, 200)

            }
            open_all_tv.isSelected=item.expand

            open_all_tv.setOnClickListener {
                item.expand = !item.expand

                notifyDataSetChanged()
            }
            if (arr.isEmpty()) {
                commit_listview_ll.visibility = View.GONE
            } else {
                commit_listview_ll.visibility = View.VISIBLE
                val list = mutableListOf<TaskMsgBean>()
                if (expand) {
                    list.addAll(arr)
                    open_all_tv.isSelected = true
                } else {
                    open_all_tv.isSelected = false
                    arr.forEachIndexed { index, taskMsgBean ->
                        if (index < 4) {
                            list.add(taskMsgBean)
                        } else {
                            return@forEachIndexed
                        }
                    }
                }
                if (arr.size>4)
                {
                    open_all_tv.visibility = View.VISIBLE
                }else
                {
                    open_all_tv.visibility = View.GONE
                }

                commit_listview.setAdapter(CommentAdapter(list))

                commit_listview.setOnItemClickListener(object : NestFullListView.OnItemClickListener {
                    override fun onLongItemClick(parent: NestFullListView?, view: View?, position: Int) {

                    }
                    override fun onItemClick(parent: NestFullListView?, view: View?, position: Int) {
                        val conttext = mContext
                        val activity: Activity = conttext as Activity
                        val intent = Intent(activity, TaskCommentActivity::class.java)
                        intent.putExtra("id", item.id.toString())
                        intent.putExtra("bounty", isbounty)
                        intent.putExtra("name", list[position].nickname)
                        intent.putExtra("b_id", list[position].u_id.toString())
                        activity.startActivityForResult(intent, 200)
                    }

                })
            }
            nameTv.text = if (TextUtils.isEmpty(username)) nickname else username
            avatar_iv.loadCircleAvatar(avatar)
            contentTv.text = content
            timeTv.text = if (TextUtils.isEmpty(create_time_text)) DateUtils.millis2String(create_time * 1000) else create_time_text
        }

    }

    override fun bindHeaderData(holder: RecyclerViewHolder) {
        super.bindHeaderData(holder)
        mHeaderListener.mbindHeaderDataAction?.let {
            it(holder)
        }
    }

    private lateinit var mHeaderListener: ListenerBuilder

    fun setmHeaderListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mHeaderListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var mbindHeaderDataAction: (RecyclerViewHolder.() -> Unit)? = null

        fun onbindHeaderData(action: RecyclerViewHolder.() -> Unit) {
            mbindHeaderDataAction = action
        }
    }

}