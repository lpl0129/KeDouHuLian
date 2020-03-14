package com.qttx.kedouhulian.ui.reward.redpacket

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.amap.api.mapcore.util.et
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.ui.reward.redpacket.adapter.RedPacketHisotyAdapter
import com.qttx.kedouhulian.ui.reward.redpacket.viewModel.RedPacketDetailViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.SpanUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.utils.extension.loadImage
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.reward_header_redpacket_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.reward_task_activity_detail.*
import java.util.concurrent.TimeUnit


class RedPacketDetailActivity : BaseActivity(), IListViewProvider {

    private val listModel: RedPacketDetailViewModel by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: RedPacketHisotyAdapter

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
        map["id"] = task_id
        listModel.getListData(this, isRefresh, map)
    }


    override fun getLayoutId(): Int = R.layout.reward_task_activity_detail
    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL

        adapter = RedPacketHisotyAdapter(listModel.list)
        adapter.setHeaderAndEmpty(true)
        adapter.addHeaderView(layoutInflater.inflate(R.layout.reward_header_redpacket_detail, null))

        adapter.setListener {
            onbindHeaderData {
                listModel.detailLiveData
                        .value?.data?.apply {
                    if (video_url == null && imgs == null) {
                        videoLl.visibility = View.GONE
                        imageIv.visibility = View.GONE
                    } else if (video_url != null) {
//                        videoplayer.post_video_play_time.text = DateUtils.formatmillTime(video_length!!.toLong())
                        videoplayer.setUp(video_url, "", JzvdStd.SCREEN_NORMAL)
                        videoplayer.thumbImageView.loadImage(imgs!!)
                        videoplayer.startVideo()
                        imageIv.visibility = View.GONE
                        videoLl.visibility = View.VISIBLE
                    } else {
                        videoLl.visibility = View.GONE
                        imageIv.visibility = View.VISIBLE
                        imageIv.loadImage(imgs!!)
                    }
                    if (link.isNotEmpty()) {
                        detailTv.visibility = View.VISIBLE
                        detailTv.setOnClickListener {
                            MineNotifyWebActivity.show(this@RedPacketDetailActivity, "", link)
                        }
                    } else {
                        detailTv.visibility = View.GONE
                    }

                    contentTv.text = content
                    priceTv.text = "¥$total_price"
                    val builder = StringBuilder()
                    if (is_top == 1) {
                        builder.append("[顶] ")
                    }
                    builder.append("[$red_type] ")
                    SpanUtils.with(titleTv)
                            .append(builder)
                            .setForegroundColor(resources.getColor(R.color.tagColor))
                            .append(title)
                            .create()

                    timeTv.text = create_time

                    leftTv.text = sycount.toString()

                    totalAcountTv.text = total_num.toString()


                    if (uid == getUserId()) {
                        //我是发布者
                        userLl.visibility = View.GONE
                        userMineLl.visibility = View.VISIBLE
                        getRedPacketLl.visibility = View.GONE
                        top_right.visibility = View.VISIBLE
                        statusTv2.visibility = View.GONE
                        statusIv.visibility = View.GONE

                        when (status.toInt()) {
                            0 -> {
                                statusIv.visibility = View.VISIBLE
                                statusIv.setImageResource(R.drawable.dakai_btn)
                                statusIv.setOnClickListener {
                                    listModel.delectTask(this@RedPacketDetailActivity, id.toString(), "-2")
                                }
                            }
                            -2 -> {
                                statusIv.visibility = View.VISIBLE
                                statusIv.setImageResource(R.drawable.butongzhi_btn)
                                statusIv.setOnClickListener {
                                    listModel.delectTask(this@RedPacketDetailActivity, id.toString(), "0")
                                }
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
//                        状态:-3=已撤销,-2=暂停,-1=待支付,0=进行中,1=已完成
                        timeMonth.text = "/ ${month}月"
                        timeDay.text = day
                    } else {
                        userLl.visibility = View.VISIBLE
                        val list = mutableListOf<String>()
                        red_pass.toCharArray().forEach {
                            list.add(it.toString())
                        }
                        kouLingLv.setAdapter(object : NestFullViewAdapter<String>(R.layout.reward_list_item_red_word, list) {
                            override fun onBind(pos: Int, t: String?, holder: NestFullViewHolder?) {
                                holder!!.setText(R.id.text, t)
                            }
                        })


                        userMineLl.visibility = View.GONE
                        getRedPacketLl.visibility = View.VISIBLE
                        top_right.visibility = View.GONE

                        userAvatarIv.loadCircleAvatar(avatar)
                        userName.text = username

                        koulingetLL.visibility = View.GONE
                        kouLingHintTv.visibility = View.GONE
                        kouLingEt.visibility = View.GONE
                        statasTagTv.visibility = View.GONE
                        getReadPacketTv.isSelected = false
                        getReadPacketTv.isEnabled = false

                        if (isget == 1) {
                            //已经领取过
                            koulingetLL.visibility = View.VISIBLE
                            kouLingHintTv.visibility = View.VISIBLE
                            kouLingHintTv.text = "已获得红包$getmoney 元"
                            statasTagTv.visibility = View.VISIBLE
                            if (limit_num == 0) {
                                statasTagTv.text = "您已参加过此活动，每人每天可抢一次"
                            } else {
                                statasTagTv.text = "您已参加过此活动，每人仅限一次"
                            }

                        } else {
                            when (status.toInt()) {
                                0 -> {
//                              "进行中"
                                    getReadPacketTv.isSelected = true
                                    getReadPacketTv.isEnabled = true
                                    //开启倒计时
                                    koulingetLL.visibility = View.VISIBLE
                                    if (listModel.hasCount || waite_time <= 0) {
                                        kouLingHintTv.visibility = View.GONE
                                        kouLingEt.visibility = View.VISIBLE
                                    } else {
                                        koulingetLL.visibility = View.VISIBLE
                                        kouLingHintTv.visibility = View.VISIBLE
                                        kouLingEt.visibility = View.GONE
                                        beginTimeCount(kouLingHintTv, kouLingEt, waite_time)
                                    }
                                    getReadPacketTv.setOnClickListener {
                                        val koulingText = kouLingEt.text.toString()
                                        if (TextUtils.isEmpty(koulingText)) {
                                            showToast("请输入口令")
                                        } else {
                                            listModel.getRedpacket(this@RedPacketDetailActivity, id.toString(), koulingText)
                                        }
                                    }
                                }
                                -2 -> {
//                                statusTv.text = "已暂停"
                                    statasTagTv.visibility = View.VISIBLE
                                    statasTagTv.text = "该广告已暂停"
                                }
                                -1 -> {
//                                statusTv.text = "待支付"
                                    statasTagTv.visibility = View.VISIBLE
                                    statasTagTv.text = "该广告待支付"
                                }
                                -3 -> {
//                                statusTv.text = "已撤销"
                                    statasTagTv.visibility = View.VISIBLE
                                    statasTagTv.text = "该广告已撤销"
                                }
                                1 -> {
//                                statusTv.text = "已完成"
                                    statasTagTv.visibility = View.VISIBLE
                                    statasTagTv.text = "该广告已完成"
                                }
                            }
                        }

                    }
                }
            }
        }
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
        listModel.detailLiveData.toObservable(this)
        {
            adapter.notifyDataSetChanged()
        }
        listModel.listManagerLiveData.toObservableList(this, this)

        listModel.getTaskLiveData.toObservable(this)
        {

            it.data?.let { bean ->
                TipDialog.newInstance("", isTip = true)
                        .setListener {
                            onTextInit {
                                val sp = SpanUtils()
                                sp.append("恭喜您获得")
                                        .append(bean.score)
                                        .setForegroundColor(resources.getColor(R.color.tagColor))
                                        .append("枚蝌蚪币,\n请在首页收取。\n\n恭喜您获得")
                                        .append(bean.money)
                                        .setForegroundColor(resources.getColor(R.color.tagColor))
                                        .append("元红包,\n")
                                        .append("现金已入账。")
                                return@onTextInit sp.create()
                            }
                        }.show(supportFragmentManager)
            }


            val intent = Intent()
            intent.putExtra("id", task_id)
            setResult(400, intent)
            ptrRequestListener(true)
        }


    }

    override fun processLogic(savedInstanceState: Bundle?) {

        setTopTitle("广告详情","结算")
        {
            //删除
            listModel.delectTask(this@RedPacketDetailActivity, task_id, "-3")
        }
        top_right.visibility = View.GONE
        task_id = intent.getStringExtra("id")
        ptrRequestListener(true)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
    }

    var disposable: Disposable? = null

    private fun beginTimeCount(tv: TextView, et: EditText, count: Int) {

        if (disposable != null) {
            return
        }
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take((count + 1).toLong())
                .map {
                    return@map count - it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .bindScheduler()
                .subscribe(object : Observer<Long> {
                    override fun onNext(t: Long) {
                        tv.text = "$t s后输入红包口令领取红包"
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        tv.visibility = View.GONE
                        et.visibility = View.VISIBLE
                        listModel.hasCount = true
                    }
                })
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
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 400) {
            when (requestCode) {
                200 ->
                    ptrRequestListener(true)
                300 ->
                    listModel.getHeaderView(this, task_id)
            }
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.resetAllVideos()
    }
}
