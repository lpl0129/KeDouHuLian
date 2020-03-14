package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskDetailViewModel
import com.qttx.kedouhulian.utils.Utils.Companion.formatmillTime
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.utils.livedata.bindScheduler
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.reward_activity_task_order_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit
import kotlin.math.sign


class TaskOrderDetailActivity : BaseActivity() {

    private val listModel: TaskDetailViewModel by viewModel()

    private lateinit var task_id: String

    private lateinit var orderid: String

    //    it.data?.let {
//        if (it.status == 0) {
//            //进行中
//            if (it.sign == 0) {
//                //未领取
//                managerFl.visibility = View.VISIBLE
//                getTaskTv.visibility = View.VISIBLE
//                submitTask.visibility = View.GONE
//            } else if (it.sign == 1) {
//                //已领取/并且未提交.或者待修改
//                if (it.finish_status == 0 || it.finish_status == 3) {
//
//                    getTaskTv.visibility = View.GONE
//                    submitTask.visibility = View.VISIBLE
//                    beginTimeCount(it.limit_time)
//                } else if (it.finish_status == -1) {
//                    top_right.visibility = View.VISIBLE
//                }
//            }
//
//        }
//    }
    override fun getLayoutId(): Int = R.layout.reward_activity_task_order_detail

    override fun liveDataListener() {

        listModel.taskOrderDeatiLiveData
                .toObservable(this)
                {

                    it.data?.orderinfo?.apply {
                        disposable?.let {
                            if (!it.isDisposed) {
                                it.dispose()
                            }
                        }
                        if (imgs_list.isEmpty()) {
                            taskorderLL.visibility = View.GONE
                        } else {
                            taskorderLL.visibility = View.VISIBLE
                            taskOrderNumberTv.text = "订单编号: $id"
                            taskOrderTimeTv.text = "时间: ${create_time_text}"
                            taskContentTv.text = content
                            taskImageShowLayout.setImageList(imgs_list)
                        }

                        if (TextUtils.isEmpty(false_info)) {
                            refuse_tv.visibility = View.GONE
                        } else {
                            refuse_tv.text = "原因:${false_info}"
                            refuse_tv.visibility = View.VISIBLE
                        }

                        managerFl.visibility = View.GONE
                        getTaskTv.visibility = View.GONE
                        submitTask.visibility = View.GONE
                        top_right.visibility = View.GONE
                        //已领取
                        when (status) {
                            0 -> {

                                managerFl.visibility = View.VISIBLE
                                taskState.text = "待提交"
                                submitTask.visibility = View.VISIBLE
                                beginTimeCount(limit_time)
                            }
                            1 ->
                                taskState.text = "待审核"
                            2 -> {
                                taskState.text = "已完成"
                                taskState.setTextColor(resources.getColor(R.color.deepColor))
                            }
                            3 -> {
                                managerFl.visibility = View.VISIBLE
//                                getTaskTv.visibility = View.VISIBLE
                                taskState.text = "待修改"
                                submitTask.visibility = View.VISIBLE
                                beginTimeCount(limit_time)
//                                taskState.setTextColor(resources.getColor(R.color.tagColor))
                            }
                            -1 -> {
                                taskState.text = "不合格"
                                taskState.setTextColor(resources.getColor(R.color.tagColor))
                                top_right.visibility = View.VISIBLE
                            }
                            -2 -> {
                                taskState.setTextColor(resources.getColor(R.color.deepColor))
                                taskState.text = "已申诉"
                            }
                            -3 -> {
                                taskState.text = "已过期"
                                taskState.setTextColor(resources.getColor(R.color.deepColor))
                            }

                        }

                    }

                    it.data?.taskinfo?.apply {

                        if (restrict_times == 0) {
                            restrictTome.visibility = View.GONE
                        } else {
                            restrictTome.visibility = View.VISIBLE
                            restrictTome.text = "每个人只限做$restrict_times 次"
                        }
                        priceDetail.text = "[完成可获得$score 个蝌蚪币]"
                        userAvatarIv.loadCircleAvatar(avatar)
                        userName.text = nickname
                        priceTv.text = task_price
                        val builder = StringBuilder()
                        if (is_top == 1) {
                            builder.append("[顶] ")
                        }
                        builder.append("[$task_typename] ")
                        SpanUtils.with(titleTv)
                                .append(builder)
                                .setForegroundColor(resources.getColor(R.color.tagColor))
                                .append(title)
                                .create()
                        if (imgs_list.isEmpty()) {
                            imageHideTv.visibility = View.GONE
                            imageShowLayout.visibility = View.GONE
                        } else {
                            imageShowLayout.visibility = View.VISIBLE
                            imageHideTv.visibility = View.GONE
                            imageShowLayout.setImageList(imgs_list)
                        }
                        contentTv.text = content
                        taskState.setTextColor(resources.getColor(R.color.primaryColor))

                    }
                }

        listModel.getTaskLiveData.toObservable(this)
        {
            showToast("领取成功")
            getData()
        }
    }

    fun getData() {

        listModel.getTaskOrderDetail(this, task_id, orderid)
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("任务详情", "申诉")
        {
            listModel.taskOrderDeatiLiveData
                    .value?.data?.orderinfo?.let {
                val intent = Intent(this@TaskOrderDetailActivity, TaskComplainActivity::class.java);
                intent.putExtra("id", orderid)
                startActivityForResult(intent, 300)
            }
        }
        top_right.visibility = View.GONE
        task_id = intent.getStringExtra("id")
        orderid = intent.getStringExtra("orderid")

        getData()
        getTaskTv.setOnClickListener {
            //修改任务
            listModel.taskOrderDeatiLiveData
                    .value?.data?.orderinfo?.let {
                val intent = Intent(this@TaskOrderDetailActivity, TaskSubmitActivity::class.java)

                val list = it.imgs.split(",")
                        .toTypedArray()
                intent.putExtra("id", it.id)
                intent.putExtra("content", it.content)
                intent.putExtra("image", list)
                intent.putExtra("cdn", it.CDN_URL)
                startActivityForResult(intent, 200)
            }
        }
        submitTask.setOnClickListener {
            //提交任务.
            listModel.taskOrderDeatiLiveData
                    .value?.data?.orderinfo?.let {
                val intent = Intent(this@TaskOrderDetailActivity, TaskSubmitActivity::class.java);
                intent.putExtra("id", it.id)
                startActivityForResult(intent, 200)
            }
        }

    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        getData()
    }

    var disposable: Disposable? = null

    private fun beginTimeCount(endTime: Long) {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        Observable.interval(1, TimeUnit.SECONDS)
                .map {
                    val now: Long = System.currentTimeMillis()
                    endTime * 1000 - now
                }
                .bindScheduler()
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        LogUtils.e("tag", "onCompleteonComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        if (t <= 0) {
                            listModel.getHeaderView(this@TaskOrderDetailActivity, task_id)
                            disposable?.let {
                                if (!it.isDisposed) {
                                    it.dispose()
                                }
                            }
                        } else {
                            val time = formatmillTime(t)
                            submitTask.text = "提交任务  $time"
                        }
                    }

                    override fun onError(e: Throwable) {
                        LogUtils.e("tag", e.cause)
                    }
                }
                )
    }

    override fun onDestroy() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200) {
            getData()
        } else if (resultCode == 400 && requestCode == 300) {
            getData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
