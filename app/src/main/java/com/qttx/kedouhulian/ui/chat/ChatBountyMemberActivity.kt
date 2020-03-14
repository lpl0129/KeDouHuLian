package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.BountyAllUserAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatBountyViewModel
import com.qttx.kedouhulian.ui.reward.buunty.BountyComplainActivity
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.GroupNotificationMessage
import io.rong.message.InformationNotificationMessage
import io.rong.message.TextMessage
import kotlinx.android.synthetic.main.chat_activity_apply_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChatBountyMemberActivity : BaseActivity(), IListViewProvider {

    private val listModel: ChatBountyViewModel by viewModel()

    private var id: String = ""

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: BountyAllUserAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.chat_activity_grouplist
    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["group_id"]=id
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = BountyAllUserAdapter(listModel.list)
        adapter.setListener {
            onItemChildClickListener { bountyUserBean, i, view ->
                when (view.id) {
                    R.id.shensuTv->{

                        val intent = Intent(this@ChatBountyMemberActivity, BountyComplainActivity::class.java)
                        intent.putExtra("order_id", bountyUserBean.id)
                        intent.putExtra("b_uid", bountyUserBean.uid)
                        intent.putExtra("groupid", id)
                        intent.putExtra("name", bountyUserBean.nickname)
                        startActivityForResult(intent, 200)

                    }
                    R.id.jieguTv -> {

                        TipDialog.newInstance("确认解雇?")
                                .setListener {
                                    onSureClick {
                                        listModel.bountyChange(this@ChatBountyMemberActivity,
                                                bountyUserBean.bounty_id.toString(), bountyUserBean.uid, i, 1
                                        )
                                    }
                                }
                                .show(supportFragmentManager)


                    }
                    R.id.guyongTv -> {
                        TipDialog.newInstance("确认雇佣?")
                                .setListener {
                                    onSureClick {
                                        listModel.bountyChange(this@ChatBountyMemberActivity,
                                                bountyUserBean.bounty_id.toString(), bountyUserBean.uid, i, 0
                                        )
                                    }
                                }
                                .show(supportFragmentManager)
                    }
                    R.id.jiesuanTv -> {
                        TipDialog.newInstance("确认结算?")
                                .setListener {
                                    onSureClick {
                                        listModel.bountyChange(this@ChatBountyMemberActivity,
                                                bountyUserBean.id, bountyUserBean.uid, i, 2
                                        )
                                    }
                                }
                                .show(supportFragmentManager)
                    }
                }
            }
        }
    }

    override fun liveDataListener() {
        listModel.bountyChangeLiveData.toObservable(this)
        {
            ptrRequestListener()
            setResult(500)
        }
        listModel.listManagerLiveData.toObservableList(this, this)

        listModel.bountyjieguLiveData.toObservable(this)
        {
            val  msg=listModel.list[listModel.pos]
                    .nickname
            val text="恭喜${msg}已被雇佣，请按要求完成工作"

            // 构造 TextMessage 实例
            val myTextMessage = InformationNotificationMessage.obtain(text)

            val myMessage = Message.obtain(id, Conversation.ConversationType.GROUP, myTextMessage)
            RongIM.getInstance().sendMessage(myMessage, null, null, object : IRongCallback.ISendMessageCallback {
                override fun onAttached(message: Message) {
                    //消息本地数据库存储成功的回调
                }

                override fun onSuccess(message: Message) {
                    //消息通过网络发送成功的回调
                }

                override fun onError(message: Message, errorCode: RongIMClient.ErrorCode) {
                    //消息发送失败的回调
                }
            })
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")
        }
        setTopTitle("全部人员")
        ptrRequestListener(true)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
    }


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }
}
