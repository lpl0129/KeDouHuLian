package com.qttx.kedouhulian.ui.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.ChatSetMarkActivity
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.imlib.model.UserInfo
import io.rong.message.InformationNotificationMessage
import kotlinx.android.synthetic.main.user_activity_user_data.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/20 0020
 */
class UserDataActivity : BaseActivity() {

    companion object {
        fun show(context: Context, id: String) {
            val intent = Intent(context, UserDataActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    private val viewModel: ChatViewModel by viewModel()
    private var user_id = ""

    override fun liveDataListener() {
        viewModel.userinfoLiveData.toObservable(this)
        {


            it.data?.let {
                avatarIv.loadCircleAvatar(it.avatar)
                usernameTv.text = it.mark
                cityTv.text = "地区: ${it.area_name}"
                nickerNameTv.text = "昵称: ${it.nickname}"
                kedouNumTv.text = "蝌蚪号: KD${it.id}"

                RongIM.getInstance().refreshUserInfoCache(UserInfo(it.id.toString(), it.mark, Uri.parse(it.avatarUrl)))

                if (it.is_friend == 1) {
                    delLl.visibility = View.VISIBLE
                    set_remarks.visibility = View.VISIBLE
                    markTv.text = it.remark
                    sendMsgLl.visibility = View.VISIBLE
                    addLL.visibility = View.GONE

                } else {
                    delLl.visibility = View.GONE
                    set_remarks.visibility = View.GONE
                    sendMsgLl.visibility = View.GONE
                    addLL.visibility = View.VISIBLE
                }
            }
        }

        viewModel.delFriendLiveData.toObservable(this)
        {

            showToast("删除成功")
            setResult(400)
            delLl.visibility = View.GONE
            set_remarks.visibility = View.GONE
            sendMsgLl.visibility = View.GONE
            addLL.visibility = View.VISIBLE
        }
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

    }

    override fun getLayoutId(): Int {
        return R.layout.user_activity_user_data
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("聊天设置")
        topTitleTv.visibility = View.GONE
        user_id = intent.getStringExtra("id")
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
            sendMsgLl -> {
                viewModel.userinfoLiveData
                        .value?.data?.let {
                    jumpToChat(this, it.id, it.mark)
                }
            }
            delLl -> {
                TipDialog.newInstance("是否删除好友?")
                        .setListener {
                            onSureClick {
                                viewModel.delFriend(this@UserDataActivity, user_id)
                            }
                        }.show(supportFragmentManager)
            }
            addLL -> {
                showEditTextDialog(user_id)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserInfoById(this, user_id)

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
        sendMsgLl.setOnClickListener(this)
        addLL.setOnClickListener(this)
        delLl.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 200 && resultCode == 400) {
//            setResult(400)
//            viewModel.getUserInfoById(this, user_id)
//        }
    }
}
