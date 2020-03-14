package com.qttx.kedouhulian.ui.reward.buunty

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.stay.toolslibrary.base.BaseActivity
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.InformationNotificationMessage
import kotlinx.android.synthetic.main.user_activity_complain.*

import org.koin.android.viewmodel.ext.android.viewModel


class BountyComplainActivity : BaseActivity() {


    private val viewModel: UserInfoViewModel by viewModel()

    private var order_id: String = ""

    private var b_uid: String = ""

    private var groupid: String = ""

    private var name: String = ""

    override fun getLayoutId(): Int = R.layout.bounty_activity_complain

    override fun processLogic(savedInstanceState: Bundle?) {
        order_id = intent.getStringExtra("order_id")
        groupid = intent.getStringExtra("groupid")
        b_uid = intent.getStringExtra("b_uid")
        name= intent.getStringExtra("name")
        submitTv.setOnClickListener {
            val phone = phoneEt.text.toString()
            val complain = complainEt.text.toString()
            viewModel.compalinBounty(this, order_id, b_uid, phone, complain)
        }
    }

    override fun liveDataListener() {
        setTopTitle("申诉")
        viewModel.complainliveData.toObservable(this)
        {


            val text="尊敬的 ${name}，因您拒绝了雇主的解雇，雇主发起了申诉，请等待并配合客服解决"

            // 构造 TextMessage 实例
            val myTextMessage = InformationNotificationMessage.obtain(text)

            val myMessage = Message.obtain(groupid, Conversation.ConversationType.GROUP, myTextMessage)
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
            showToast("提交成功")
            setResult(400)
            finish()
        }

    }


}
