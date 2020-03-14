package com.qttx.kedouhulian.ui.reward.buunty

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.BountyDetailViewModel
import com.qttx.kedouhulian.ui.reward.task.TaskCommentActivity
import com.qttx.kedouhulian.ui.reward.task.adapter.TaskMsgAdapter
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.*
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.chat_fragment_home_tab.*
import kotlinx.android.synthetic.main.reward_activity_bounty_detail.*
import kotlinx.android.synthetic.main.reward_header_bounty_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class BountyDetailActivity : BaseActivity(), IListViewProvider {
    private val listModel: BountyDetailViewModel by viewModel()

    private lateinit var bounty_id: String

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["bounty_id"] = bounty_id
        listModel.getListData(this, isRefresh, map)
    }


    override fun getLayoutId(): Int = R.layout.reward_activity_bounty_detail

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL

        adapter = TaskMsgAdapter(listModel.list)
        adapter.isbounty=true
        adapter.setHeaderAndEmpty(true)
        adapter.addHeaderView(layoutInflater.inflate(R.layout.reward_header_bounty_detail, null))
        adapter.setmHeaderListener {
            onbindHeaderData {
                listModel.deatiLiveData
                        .value?.data?.apply {
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
                            .setForegroundColor(resources.getColor(R.color.tagColor))
                            .append(title)
                            .create()

                    if (imgs_list.isEmpty()) {
                        imageHideTv.visibility = View.GONE
                        imageShowLayout.visibility = View.GONE
                    } else if (hidden_img == 0 || is_puter == 1||"1"==current_status) {
                        imageShowLayout.visibility = View.VISIBLE
                        imageHideTv.visibility = View.GONE
                        imageShowLayout.setImageList(imgs_list)
                    } else {
                        imageShowLayout.visibility = View.GONE
                        imageHideTv.visibility = View.VISIBLE
                    }
                    guyongTv.text = employ_num.toString()
                    baomingTv.text = submit_num.toString()
                    countTv.text = total_num.toString()
                    contentTv.text = content


                    if (is_puter == 1) {
                        //我是发布者
                        userLl.visibility = View.GONE
                        userMineLl.visibility = View.VISIBLE
                        top_right.visibility = View.VISIBLE
                        statusTv2.visibility = View.GONE
                        statusIv.visibility = View.GONE
                        when (status) {
                            0 -> {
                                statusIv.visibility = View.VISIBLE
                                statusIv.setImageResource(R.drawable.dakai_btn)
                                statusIv.setOnClickListener {
                                    listModel.delectTask(this@BountyDetailActivity, id.toString(), "-2")
                                }
                                statusTv2.visibility = View.VISIBLE
                                statusTv2.text = "进行中"
                            }
                            -2 -> {
                                statusIv.visibility = View.VISIBLE
                                statusIv.setImageResource(R.drawable.butongzhi_btn)
                                statusIv.setOnClickListener {
                                    listModel.delectTask(this@BountyDetailActivity, id.toString(), "0")
                                }
                                statusTv2.visibility = View.VISIBLE
                                statusTv2.text = "已暂停"
                            }
                            -1 -> {
                                statusTv2.visibility = View.VISIBLE
                                statusTv2.text = "待支付"
                            }
                            -3 -> {
                                statusTv2.visibility = View.VISIBLE
                                statusTv2.text = "已撤销"
                                top_right.visibility = View.GONE
                            }
                            1 -> {
                                statusTv2.visibility = View.VISIBLE
                                statusTv2.text = "已完成"
                            }
                        }
                        val cal = Calendar.getInstance()
                        cal.time = Date(create_time * 1000)
                        val month = cal.get(Calendar.MONTH) + 1
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        timeMonth.text = "/ $month 月"
                        timeDay.text = day.toString()

                    } else {
                        userMineLl.visibility = View.GONE
                        userLl.visibility = View.VISIBLE
                    }
                }
                liuyanTv.setOnClickListener {
                    val intent = Intent(this@BountyDetailActivity, TaskCommentActivity::class.java)
                    intent.putExtra("id", bounty_id)
                    intent.putExtra("bounty", true)
                    startActivityForResult(intent, 200)
                }

            }
        }

    }

    override fun liveDataListener() {

        listModel.listManagerLiveData.toObservableList(this, this)
        listModel.delectLiveData.toObservable(this)
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
                        }.show(supportFragmentManager)
            }
            ptrRequestListener(true)
        }
        listModel.deatiLiveData.toObservable(this)
        {
            adapter.notifyDataSetChanged()
            it.data?.apply {
                if (is_puter == 1) {
                    //我是发布者
                    managerFl.visibility = View.GONE
                } else {
                    managerFl.visibility = View.VISIBLE
                    if ("1"==current_status ) {
                        getTaskTv.visibility = View.GONE
                        submitTask.visibility = View.VISIBLE
                    } else {
                        getTaskTv.visibility = View.VISIBLE
                        submitTask.visibility = View.GONE
                    }
                }
            }
        }

        listModel.getTaskLiveData.toObservable(this)
        {
            showToast("报名成功")
            val intent=Intent()
            intent.putExtra("id",bounty_id)
            setResult(400,intent)
            listModel.deatiLiveData
                    .value?.data?.let {
                RongIM.getInstance().startGroupChat(this@BountyDetailActivity,
                        it.group_id, "悬赏报名群")
            }
            ptrRequestListener(true)
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("悬赏详情","结算")
        {
            //删除
            listModel.delectTask(this@BountyDetailActivity, bounty_id, "-3")
        }
        top_right.visibility = View.GONE
        bounty_id = intent.getStringExtra("id")
        ptrRequestListener(true)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==200)
        {
            ptrRequestListener( true)
        }else if (resultCode == 400&&requestCode==300)
        {
            listModel.getHeaderView(this, bounty_id)
        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
    }

    private lateinit var adapter: TaskMsgAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout

    override val recyclerViewProvider: RecyclerView
        get() = recyclerView


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            getTaskTv -> {
                listModel.getTask(this, bounty_id)
            }
        }
    }

    override fun initViewClickListeners() {
        getTaskTv.setOnClickListener(this)
    }

}
