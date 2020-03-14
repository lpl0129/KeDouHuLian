package com.qttx.kedouhulian.ui.chat

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.message.RedMessage
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.BaseTextWatcher
import com.stay.toolslibrary.utils.LogUtils
import io.rong.imkit.RongIM
import io.rong.imkit.userInfoCache.RongUserInfoManager
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.chat_activity_zhuanzhang.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/16 0016
 */
class ZhuanZhangActivity : BaseActivity() {

    private val viewModel: WalletViewModel by viewModel()

    private val userViewModel: UserInfoViewModel by viewModel()

    private var userid = ""

    override fun getLayoutId(): Int {
        return R.layout.chat_activity_zhuanzhang
    }

    private var availableAmount: Double = 0.0

    private lateinit var textWatcher: BaseTextWatcher

    override fun processLogic(savedInstanceState: Bundle?) {

        setTopTitle("")
        userid = intent.getStringExtra("id")

//        userViewModel.getUserInfoById(this,userid)

        val userInfo = RongUserInfoManager.getInstance().getUserInfo(userid)
        val options = RequestOptions.placeholderOf(com.stay.basiclib.R.drawable.user_avatar_default)
        val newOptions = options.error(com.stay.basiclib.R.drawable.user_avatar_default).circleCrop()
        Glide.with(this).load(userInfo.portraitUri).apply(newOptions).into(avatarIv)
        usernameTv.text = userInfo.name
        textWatcher = BaseTextWatcher()
        moneyEt.addTextChangedListener(textWatcher)
        post_pay.setOnClickListener { pay() }
    }

    override fun liveDataListener() {
        viewModel.transferLiveData
                .toObservable(this)
                {
                    sendRedBag(viewModel.hasSendMoney)
                }

    }


    private fun pay() {

        val text = moneyEt.text.toString()
        if (text.isEmpty()) {
            showToast("请输入转账金额")
        } else {
                viewModel.transter_money(this, text, userid)

        }

    }

    private fun sendRedBag(money: String) {

        val message = RedMessage()
        message.money = money
        // 构造 TextMessage 实例

        val myMessage = Message.obtain(userid, Conversation.ConversationType.PRIVATE, message)

        RongIM.getInstance().sendMessage(myMessage, null, null, object : IRongCallback.ISendMessageCallback {
            override fun onAttached(message: Message) {
                //消息本地数据库存储成功的回调
            }

            override fun onSuccess(message: Message) {
                //消息通过网络发送成功的回调
                showToast("转账成功")
                finish()
            }

            override fun onError(message: Message, errorCode: RongIMClient.ErrorCode) {
                //消息发送失败的回调
                LogUtils.e("tag", errorCode);
            }
        })

    }
}