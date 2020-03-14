package com.qttx.kedouhulian.ui.chat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.amap.api.mapcore.util.it
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.ChatSearchAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.InformationNotificationMessage
import kotlinx.android.synthetic.main.chat_activity_search_user.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChatSearchUserActivity : BaseActivity() {

    private val viewModel: ChatViewModel by viewModel()

    private lateinit var adapter: ChatSearchAdapter

    override fun getLayoutId(): Int = R.layout.chat_activity_search_user

    override fun processLogic(savedInstanceState: Bundle?) {

    }

    override fun initBeforeListener() {
        super.initBeforeListener()
        adapter = ChatSearchAdapter(viewModel.list)
        adapter.setListener {
            onItemChildClickListener { searchBean, i, view ->

                if (searchBean.type == 1) {
                    showEditTextDialog2(searchBean.group_id)
                } else {
                    showEditTextDialog(searchBean.uid.toString())
                }

            }
        }
        val mLayoutManager = LinearLayoutManager(this)
        //垂直方向
        mLayoutManager.orientation = OrientationHelper.VERTICAL
        //给RecyclerView设置布局管理器
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter
    }


    private var popupWindow: BasePopupView? = null
    private fun showEditTextDialog(id: String) {
        popupWindow = XPopup
                .Builder(this)
                .autoDismiss(true)
                .autoOpenSoftInput(true)
                .asInputConfirm("添加好友", "", "请输入备注信息")
                {
                    popupWindow?.dismiss()
                    val map = mutableMapOf<String, String>()
                    map["uid"] = id
                    if (it.isNotEmpty()) {
                        map["content"] = it
                    }
                    viewModel.applyAddFriend(this, map)
                }.show()
    }

    private var popupWindow2: BasePopupView? = null
    private fun showEditTextDialog2(id: String) {
        popupWindow2 = XPopup
                .Builder(this)
                .autoDismiss(true)
                .autoOpenSoftInput(true)
                .asInputConfirm("申请入群", "", "请输入备注信息")
                {
                    popupWindow?.dismiss()
                    val map = mutableMapOf<String, String>()
                    map["group_id"] = id
                    map["uid"] = getUserId()
                    if (it.isNotEmpty()) {
                        map["content"] = it
                    }
                    viewModel.applyAddGroup(this, map)
                }.show()
    }

    override fun liveDataListener() {
        viewModel.searchLiveData
                .toObservable(this)
                {
                    viewModel.list.clear()
                    it.data?.let {
                        viewModel.list.addAll(it)
                    }
                    adapter.isInitEmpty=false
                    adapter.notifyDataSetChanged()
                }
        viewModel.applyAddFriendLiveData.toObservable(this)
        {
            setResult(400)
            if (it.data!=null)
            {
                if (it.data==1)
                {
                    showToast("已申请")
                }else
                {

                    // 构造 TextMessage 实例
                    val myTextMessage = InformationNotificationMessage.obtain("你们已经成为好友,可以开始聊天了")

                    val myMessage = Message.obtain(viewModel.userid, Conversation.ConversationType.PRIVATE, myTextMessage)
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
                    showToast("添加成功")
                    viewModel.searchUserOrGroup(this, searchEt.text.toString())
                }
            }else
            {
                showToast("已申请")
            }
        }
        viewModel.applyAddGroupLiveData.toObservable(this)
        {
            showToast("已申请 ")
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            topBack -> {
                finish()
            }
            searchTv -> {
                viewModel.searchUserOrGroup(this, searchEt.text.toString())
            }
        }
    }

    override fun initViewClickListeners() {
        topBack.setOnClickListener(this)
        searchTv.setOnClickListener(this)
    }
}
