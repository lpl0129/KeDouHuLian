package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.text.TextUtils
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.BountyUserBean
import com.qttx.kedouhulian.ui.chat.adapter.BountyUserAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.ui.reward.buunty.BountyComplainActivity
import com.qttx.kedouhulian.utils.OperationRong
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import io.rong.imkit.userInfoCache.RongUserInfoManager
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Group
import io.rong.imlib.model.UserInfo
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import io.rong.imlib.RongIMClient
import io.rong.imlib.IRongCallback
import io.rong.message.GroupNotificationMessage
import io.rong.message.InformationNotificationMessage
import kotlinx.android.synthetic.main.chat_list_item_reward.*
import kotlinx.android.synthetic.main.user_activity_conversation.*


/**
 * Created by huang on 2017/9/4.
 */

class ConversationActivity : BaseActivity() {

    val viewModel: ChatViewModel by viewModel()

    private var adapter: BountyUserAdapter? = null

    override fun liveDataListener() {
        viewModel.userinfoLiveData.toObservable(this)
        {
            it.data?.let {
                RongIM.getInstance().refreshUserInfoCache(UserInfo(it.id.toString(), it.mark, Uri.parse(it.avatarUrl)))
                topTitleTv.text = it.mark
            }

        }
        viewModel.grioupInfoLiveData.toObservable(this)
        {
            it.data?.let {
                RongIM.getInstance().refreshGroupInfoCache(Group(it.group_id, it.group_name, Uri.parse(it.group_avatar)))
                topTitleTv.text = "${it.group_name} (${it.list.size})"
                if (it.group_type == 2) {
                    OperationRong.setConversationTop(this, Conversation.ConversationType.GROUP, id, true)
                    if (it.is_grouper == 1) {
                        viewModel.bountyFromGroup(this@ConversationActivity, id)
                    }
                } else {
                    recyclerView.visibility = View.GONE
                }
            }

        }
        viewModel.bountyMemberFromGroupLiveData.toObservable(this)
        {
            it.data?.let {
                viewModel.bountyList.clear()
                it.list?.let {
                    viewModel.bountyList.addAll(it)
                }
                viewModel.bountyList.add(BountyUserBean())

                if (adapter == null) {
                    adapter = BountyUserAdapter(viewModel.bountyList)
                    adapter!!.setListener {
                        onItemChildClickListener { bountyUserBean, i, view ->
                            when (view.id) {
                                R.id.shensuTv -> {

                                    val intent = Intent(this@ConversationActivity, BountyComplainActivity::class.java)
                                    intent.putExtra("order_id", bountyUserBean.id)
                                    intent.putExtra("b_uid", bountyUserBean.uid)
                                    intent.putExtra("groupid", id)
                                    intent.putExtra("name", bountyUserBean.nickname)
                                    startActivityForResult(intent, 200)

                                }
                                R.id.userLl -> {

                                    val intent = Intent(this@ConversationActivity, ChatBountyMemberActivity::class.java)
                                    intent.putExtra("id", id)
                                    startActivityForResult(intent, 200)

                                }
                                R.id.jiesuanTv -> {
                                    TipDialog.newInstance("确认结算?")
                                            .setListener {
                                                onSureClick {
                                                    viewModel.bountyChange(this@ConversationActivity,
                                                            bountyUserBean.id, bountyUserBean.uid, i, 2
                                                    )
                                                }
                                            }
                                            .show(supportFragmentManager)
                                }
                                R.id.guyongTv -> {
                                    TipDialog.newInstance("确认雇佣?")
                                            .setListener {
                                                onSureClick {
                                                    viewModel.bountyChange(this@ConversationActivity,
                                                            bountyUserBean.bounty_id.toString(), bountyUserBean.uid, i, 0
                                                    )
                                                }
                                            }
                                            .show(supportFragmentManager)
                                }
                                R.id.jieguTv -> {
                                    TipDialog.newInstance("确认解雇?")
                                            .setListener {
                                                onSureClick {
                                                    viewModel.bountyChange(this@ConversationActivity,
                                                            bountyUserBean.bounty_id.toString(), bountyUserBean.uid, i, 1
                                                    )
                                                }
                                            }
                                            .show(supportFragmentManager)
                                }
                            }

                        }
                    }
                    val manager = LinearLayoutManager(this@ConversationActivity)
                    manager.orientation = OrientationHelper.HORIZONTAL
                    recyclerView.layoutManager = manager
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                } else {
                    adapter?.notifyDataSetChanged()
                }

            }
        }
        viewModel.bountyChangeLiveData.toObservable(this)
        {
            viewModel.bountyFromGroup(this@ConversationActivity, id)
        }
        viewModel.bountyjieguLiveData.toObservable(this)
        {
            viewModel.bountyFromGroup(this@ConversationActivity, id)
            val msg = viewModel.bountyList[viewModel.pos]
                    .nickname
            val text = "恭喜${msg}已被雇佣，请按要求完成工作"

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

    var type = "private"
    /**
     * 	群类型:0=普通群,1=收费群,2=悬赏群
     */
    var groutType = 0

    override fun getLayoutId(): Int {
        return R.layout.user_activity_conversation
    }

    private var id: String = ""
    override fun processLogic(savedInstanceState: Bundle?) {
        id = intent.data!!.getQueryParameter("targetId")
        type = Conversation.ConversationType.valueOf(intent.data.lastPathSegment.toUpperCase(Locale.US)).getName()


        val title = getTitle(id)
        setTopTitle(title, R.drawable.more_icon)
        {
            if (type == "group") {
                val intent = Intent(this, ChatGroupDetailActivity::class.java)
                intent.putExtra("id", id)
                startActivityForResult(intent, 200)
            } else {
                val intent = Intent(this, ChatPrivateSettingActivity::class.java)
                intent.putExtra("id", id)
                startActivityForResult(intent, 200)
            }
        }
        if (type == "private") {
            viewModel.getUserInfoById(this, id!!)
        } else if (type == "group") {
            //刷新群聊
            viewModel.getGroupInfoById(this, id!!)
        }
    }


    /**
     * 设置私聊界面 ActionBar..只判断了 群聊和私聊
     */
    private fun getTitle(targetId: String?): String {

        var title = intent.data!!.getQueryParameter("title")
        if (!TextUtils.isEmpty(title)) {
            if (title == "null") {
                if (!TextUtils.isEmpty(targetId)) {
                    if (type == "private") {
                        val userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId)
                        if (userInfo != null) {
                            title = userInfo.name
                        }
                    } else {
                        val userInfo = RongUserInfoManager.getInstance().getGroupInfo(targetId)
                        if (userInfo != null) {
                            title = userInfo.name
                        }
                    }
                }
            }
        }
        return title
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 400) {
            if (type == "private") {
                viewModel.getUserInfoById(this, id!!)
            } else if (type == "group") {
                //刷新群聊
                viewModel.getGroupInfoById(this, id!!)
            }
        } else if (resultCode == 600) {
            finish()
        } else if (resultCode == 500) {
            viewModel.bountyFromGroup(this@ConversationActivity, id)
        } else if (resultCode == 300) {
            // 构造 TextMessage 实例
            val myTextMessage = TextMessage.obtain(data!!.getStringExtra("text"))

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

}
