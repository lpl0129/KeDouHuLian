package com.qttx.kedouhulian.ui.reward.task

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.adapter.TaskMsgAdapter
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskDetailViewModel
import com.qttx.kedouhulian.utils.Utils.Companion.formatmillTime
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.utils.livedata.bindScheduler
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.reward_header_task_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.reward_task_activity_detail.*
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit


class TaskHomeDetailActivity : BaseActivity(), IListViewProvider {

    private val listModel: TaskDetailViewModel by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: TaskMsgAdapter

    private lateinit var layoutManger: LinearLayoutManager

    private lateinit var task_id: String

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["task_id"] = task_id
        listModel.getListData(this, isRefresh, map)
    }

    override fun getLayoutId(): Int = R.layout.reward_task_activity_detail
    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL

        adapter = TaskMsgAdapter(listModel.list)
        adapter.setHeaderAndEmpty(true)
        adapter.addHeaderView(layoutInflater.inflate(R.layout.reward_header_task_detail, null))
        adapter.setmHeaderListener {
            onbindHeaderData {
                listModel.taskDeatiLiveData
                        .value?.data?.apply {

                    if (restrict_times == 0) {
                        restrictTome.visibility = View.GONE
                    } else {
                        restrictTome.visibility = View.VISIBLE
                        restrictTome.text = "每个人只限做$restrict_times 次"
                    }

                    priceDetail.text = "[完成可获得$score 个蝌蚪币]"
                    userAvatarIv.loadCircleAvatar(avatar)
                    userName.text = username
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

                    } else if (show_img == 1 || sign == 1 || sign == 2) {
                        imageShowLayout.visibility = View.VISIBLE
                        imageHideTv.visibility = View.GONE
                        imageShowLayout.setImageList(imgs_list)
                    } else {
                        imageShowLayout.visibility = View.GONE
                        imageHideTv.visibility = View.VISIBLE
                    }
                    contentTv.text = content
                    taskState.setTextColor(resources.getColor(R.color.primaryColor))
                }
                liuyanTv.setOnClickListener {
                    val intent = Intent(this@TaskHomeDetailActivity, TaskCommentActivity::class.java)
                    intent.putExtra("id", task_id)
                    startActivityForResult(intent, 200)
                }

            }
        }
        listModel.taskDeatiLiveData.toObservable(this)
        {
            adapter.notifyDataSetChanged()
            managerFl.visibility = View.GONE
            disposable?.let {
                if (!it.isDisposed) {
                    it.dispose()
                }
            }
            it.data?.let {


                if (it.status == 0) {
                    //进行中
                    if (it.sign == 0) {
                        //未领取
                        managerFl.visibility = View.VISIBLE
                        getTaskTv.visibility = View.VISIBLE
                        getTaskTv.isEnabled = true
                        getTaskTv.setText("领取任务")
                        getTaskTv.setBackgroundResource(R.drawable.primary_15_bk)
                        submitTask.visibility = View.GONE
                    } else if (it.sign == 1) {
                        //未提交
                        if (it.finish_status == 0) {
                            managerFl.visibility = View.VISIBLE
                            getTaskTv.visibility = View.GONE
                            submitTask.visibility = View.VISIBLE
                            beginTimeCount(it.limit_time)
                        } else if (it.finish_status == -3 || it.finish_status == -1 || it.finish_status == -2 || it.finish_status == 2) {
                            //可以再次领取
                            managerFl.visibility = View.VISIBLE
                            getTaskTv.visibility = View.VISIBLE
                            getTaskTv.isEnabled = true
                            getTaskTv.setText("领取任务")
                            getTaskTv.setBackgroundResource(R.drawable.primary_15_bk)
                            submitTask.visibility = View.GONE
                        } else if (it.finish_status == 3) {
                            getTaskTv.isEnabled = false
                            getTaskTv.setText("请先修改提交的任务")
                            getTaskTv.setBackgroundResource(R.drawable.spite_15_bk)
                        }
                    }

                }
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)

        listModel.getTaskLiveData.toObservable(this)
        {
            showToast("领取成功")
            listModel.getHeaderView(this, task_id)
        }
        listModel.delectLiveData.toObservable(this)
        {
            ptrRequestListener(true)
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("任务详情", "申诉")
        {
            listModel.taskDeatiLiveData
                    .value?.data?.let {
                val intent = Intent(this@TaskHomeDetailActivity, TaskComplainActivity::class.java);
                intent.putExtra("id", it.orderid.toString())
                startActivityForResult(intent, 300)
            }
        }
        top_right.visibility = View.GONE
        task_id = intent.getStringExtra("id")
        ptrRequestListener(true)
        getTaskTv.setOnClickListener {
            listModel.getTask(this, task_id)
        }
        submitTask.setOnClickListener {
            //提交任务.
            listModel.taskDeatiLiveData
                    .value
                    ?.data?.let {
                val intent = Intent(this@TaskHomeDetailActivity, TaskSubmitActivity::class.java)
                intent.putExtra("id", it.orderid.toString())
                startActivityForResult(intent, 200)
            }
        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
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
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        if (t <= 0) {
                            listModel.getHeaderView(this@TaskHomeDetailActivity, task_id)
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
        Activity.RESULT_OK
        if (requestCode == 200) {
            ptrRequestListener(true)
        } else if (resultCode == 400 && requestCode == 300) {
            listModel.getHeaderView(this, task_id)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
