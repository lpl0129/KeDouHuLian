package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskDetailViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.widget.dialog.TipDialog
import kotlinx.android.synthetic.main.reward_activity_my_publish_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.StringBuilder
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 * 我自己的任务详情
 */
class MyTaskDeatilActivity : BaseActivity() {

    val viewModel: TaskDetailViewModel by viewModel()

    private lateinit var taskid: String


    override fun getLayoutId(): Int =
            R.layout.reward_activity_my_publish_detail

    override fun processLogic(savedInstanceState: Bundle?) {
        taskid = intent.getStringExtra("id")
        viewModel.getHeaderView(this, taskid)
        setTopTitle("任务详情", "结算")
        {
            TipDialog.newInstance("是否结算该任务", "提示")
                    .setListener {
                        onSureClick {
                            viewModel.delectTask(this@MyTaskDeatilActivity, taskid)
                        }
                    }.show(supportFragmentManager)
        }
        desNumLl.setOnClickListener {
            //
            viewModel.taskDeatiLiveData.value?.data?.apply {
                val intent = Intent(this@MyTaskDeatilActivity, WaitVerifyTaskActivity::class.java)
                intent.putExtra("taskid",id.toString())
                startActivityForResult(intent, 200)
            }
        }
    }

    /**
     * 设置liveDATA网络请求监听
     */
    override fun liveDataListener() {
        viewModel.taskDeatiLiveData.toObservable(this)
        {
            setData()
        }
        viewModel.delectLiveData.toObservable(this)
        {

            it.data?.let { bean ->
                TipDialog.newInstance("", isTip = true)
                        .setListener {
                            onTextInit {
                                val sp = SpanUtils()
                                sp.append("本次结算退回服务费")
                                        .append(bean.fee)
                                        .setForegroundColor(resources.getColor(R.color.tagColor))
                                        .append("元,\n\n赏金")
                                        .append(bean.sy_money)
                                        .setForegroundColor(resources.getColor(R.color.tagColor))
                                        .append("元")
                                return@onTextInit sp.create()
                            }
                            onDisMiss {
                                setResult(400)
                                finish()
                            }
                        }.show(supportFragmentManager)
            }
        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getHeaderView(this, taskid)
    }

    private fun setData() {
        viewModel.taskDeatiLiveData.value?.data?.apply {
            val builder = StringBuilder()
            if (is_top == 1) {
                builder.append("[顶] ")
            }
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()
            imageShowLayout.setImageList(imgs_list)
            val cal = Calendar.getInstance()
            cal.time = Date(create_time * 1000)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            timeMonth.text = " /$month 月"
            timeDay.text = day.toString()
            dsnumTv.text = "${dsnum}个"
            rateTv.text = rate.toString()
            allAcountTv.text = "总数量: $total_num"

            priceTv.text = task_price
            contentTv.text = content
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            viewModel.getHeaderView(this, taskid)
        }
    }
}