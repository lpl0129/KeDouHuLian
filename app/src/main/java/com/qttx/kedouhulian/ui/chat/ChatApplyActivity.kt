package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.ChatApplyAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatApplyViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.InformationNotificationMessage
import io.rong.message.TextMessage
import kotlinx.android.synthetic.main.chat_activity_apply_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChatApplyActivity : BaseActivity(), IListViewProvider {

    private val listModel: ChatApplyViewModel by viewModel { parametersOf(id) }

    private var id: String = ""
    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: ChatApplyAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.chat_activity_apply_detail
    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        if (id.isNotEmpty())
        {
            map["group_id"]=id
        }
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = ChatApplyAdapter(listModel.list)
        adapter.setListener {
            onItemChildClickListener { chatApplyBean, i, view ->
                val id = if (id.isEmpty()) chatApplyBean.fmid else chatApplyBean.mid
                listModel.userid=chatApplyBean.uid
                listModel.agreeApply(this@ChatApplyActivity, id)
            }
        }
    }

    override fun liveDataListener() {
        listModel.managerLiveData.toObservable(this)
        {

             listModel.userid
                     ?.let {
                         if (it.isNotEmpty())
                         {
                             // 构造 TextMessage 实例
                             val myTextMessage = InformationNotificationMessage.obtain("你们已经成为好友,可以开始聊天了")

                             val myMessage = Message.obtain(listModel.userid, Conversation.ConversationType.PRIVATE, myTextMessage)

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
            ptrRequestListener()
            setResult(400)
        }
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    override fun processLogic(savedInstanceState: Bundle?) {
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")
        }
        listModel.group_id=id;
        if (TextUtils.isEmpty(id)) {
            setTopTitle("好友审批")
            topView.visibility = View.GONE
            topBack.setOnClickListener { finish() }
            searchEt.setOnClickListener {
                val intent = Intent(this, ChatSearchUserActivity::class.java)
                startActivityForResult(intent, 200)
            }
            searchTv.setOnClickListener {
                val intent = Intent(this, ChatSearchUserActivity::class.java)
                startActivityForResult(intent, 200)
            }

        } else {
            setTopTitle("入群审批")
            topHeader.visibility = View.GONE
            newFirendLl.visibility = View.GONE
        }
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
