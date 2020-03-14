package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.qttx.kedouhulian.utils.OperationRong
import com.qttx.kedouhulian.utils.Utils
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.PreferenceUtil
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.InformationNotificationMessage
import kotlinx.android.synthetic.main.chat_activity_private_setting.*
import kotlinx.android.synthetic.main.chat_activity_private_setting.addLL
import kotlinx.android.synthetic.main.chat_activity_private_setting.delLl
import kotlinx.android.synthetic.main.chat_activity_private_setting.markTv
import kotlinx.android.synthetic.main.chat_activity_private_setting.set_remarks
import kotlinx.android.synthetic.main.user_activity_user_data.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/14 0014
 */
class ChatPrivateSettingActivity : BaseActivity() {
    private val viewModel: ChatViewModel by viewModel()
    private var user_id = ""
    private var isOpen = false

    override fun liveDataListener() {
        viewModel.applyAddFriendLiveData.toObservable(this)
        {
            if (it.data != null) {
                if (it.data == 1) {
                    showToast("已申请")
                } else {
                    showToast("添加成功")
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
                    viewModel.getUserInfoById(this, user_id)
                }
            } else {
                showToast("已申请")
            }
        }

        viewModel.userinfoLiveData.toObservable(this)
        {
            it.data?.let {

                markTv.text = it.remark
                if (it.is_friend==1)
                {
                    delLl.visibility=View.VISIBLE
                    set_remarks.visibility=View.VISIBLE
                    addLL.visibility = View.GONE
                }else
                {
                    delLl.visibility=View.GONE
                    set_remarks.visibility=View.GONE
                    addLL.visibility = View.VISIBLE
                }
            }

        }
        viewModel.delFriendLiveData.toObservable(this)
        {

            showToast("删除成功")
            delLl.visibility=View.GONE
            set_remarks.visibility=View.GONE
            addLL.visibility = View.VISIBLE
//            setResult(500)
//            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.chat_activity_private_setting
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("聊天设置")
        user_id = intent.getStringExtra("id")
        viewModel.getUserInfoById(this, user_id)
        //获取是否免打扰
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE, user_id, object : RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            override fun onSuccess(status: Conversation.ConversationNotificationStatus) {
                isOpen = status.value ==0//不打扰为0
                setImageViewState()
            }

            override fun onError(errorCode: RongIMClient.ErrorCode) {}
        })
    }

    fun setImageViewState() {
        if (isOpen) {
            notifyIv.setImageResource(R.drawable.dakai_btn)

        } else {
            notifyIv.setImageResource(R.drawable.butongzhi_btn)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            set_remarks -> {
                val intent = Intent(this, ChatSetMarkActivity::class.java)
                intent.putExtra("id", user_id)
                intent.putExtra("name", markTv.text.toString())
                startActivityForResult(intent, 200)
            }
            notifyLl -> {
                isOpen = !isOpen
                setImageViewState()
                OperationRong.setConversationNotification(this, Conversation.ConversationType.PRIVATE, user_id, isOpen)
            }
            clearHistoryLl -> {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE,
                        user_id, object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean?) {
                        showToast("已清空聊天记录")
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode) {

                    }
                })
                RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.PRIVATE,
                        user_id, System.currentTimeMillis(), null)
            }
            delLl -> {

                TipDialog.newInstance("是否删除好友?")
                        .setListener {
                            onSureClick {
                                viewModel.delFriend(this@ChatPrivateSettingActivity, user_id)
                            }
                        }.show(supportFragmentManager)
            }
            addLL -> {
                showEditTextDialog(user_id)
            }
        }
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
    override fun initViewClickListeners() {
        set_remarks.setOnClickListener(this)
        notifyLl.setOnClickListener(this)
        clearHistoryLl.setOnClickListener(this)
        delLl.setOnClickListener(this)
        addLL.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 400) {
            setResult(400)
            viewModel.getUserInfoById(this, user_id)
        }
    }


}