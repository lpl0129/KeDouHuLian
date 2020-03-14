package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.text.TextUtils
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.FriendBean
import com.qttx.kedouhulian.ui.chat.adapter.ChatContactAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.PinyinUtils
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.chat_activity_firend.*
import kotlinx.android.synthetic.main.chat_invitetogroup_activity.*
import kotlinx.android.synthetic.main.chat_invitetogroup_activity.bar
import kotlinx.android.synthetic.main.chat_invitetogroup_activity.recyclerView
import kotlinx.android.synthetic.main.chat_invitetogroup_activity.select
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
/**
 * 邀请好友入群
 */
class ChatInviteToGroupActivity : BaseActivity() {

    private val viewModel: ChatViewModel by viewModel()

    private lateinit var adapter: ChatContactAdapter

    private var groupUserList: ArrayList<String>? = null

    /**
     * 0,私有群
     * 1.公共群
     */
    private var group_type = 0

    /**
     *是否是群主
     */
    private var isGroupOwner = false

    private var group_id: String = ""

    private lateinit var mLayoutManager: LinearLayoutManager
    override fun getLayoutId(): Int {
        return R.layout.chat_invitetogroup_activity

    }

    override fun initBeforeListener() {
        super.initBeforeListener()
        adapter = ChatContactAdapter(viewModel.friendList)
        adapter.setListener {
            onItemClickListener { friendBean, i, view ->
                if (friendBean.canSelect) {
                    friendBean.select = !friendBean.select
                    adapter.notifyDataSetChanged()
                    if (!checkHasSelect()) {
                        agreeTv.setBackgroundResource(R.drawable.primary_5_bk)
                        agreeTv.isEnabled = true
                    } else {
                        agreeTv.setBackgroundResource(R.drawable.tint_primary_5_bk)
                        agreeTv.isEnabled = false
                    }
                }
            }
        }
        mLayoutManager = LinearLayoutManager(this)
        //垂直方向
        mLayoutManager.orientation = OrientationHelper.VERTICAL
        //给RecyclerView设置布局管理器
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter
    }


    override fun processLogic(savedInstanceState: Bundle?) {
        isGroupOwner = intent.getBooleanExtra("owner", false)
        group_type = intent.getIntExtra("type", 0)
        if (intent.hasExtra("list")) {
            groupUserList = intent.getStringArrayListExtra("list")
        }
        if (intent.hasExtra("id")) {
            group_id = intent.getStringExtra("id")
        }

        if (group_id.isEmpty()) {
            top_title.text = "创建群聊"
        } else {
            top_title.text = "邀请入群"
        }

        viewModel.getFirendList(this)
        cancleTv.setOnClickListener {
            finish()
        }
        agreeTv.setBackgroundResource(R.drawable.tint_primary_5_bk)
        agreeTv.isEnabled = false
        agreeTv.setOnClickListener {
            val uid = StringBuilder()

            viewModel.friendList.filter {
                it.select
            }.forEach {
                uid.append(it.uid)
                        .append(",")
            }
            val map = mutableMapOf<String, String>()
            val uidtext=uid.deleteCharAt(uid.length - 1).toString()
            map["uid"] = uidtext
            map["uids"] = uidtext
            if (group_id.isNotEmpty()) {
                map["group_id"] = group_id
                if (isGroupOwner) {
                    //群主直接拉
                    viewModel.joInToAddGroup(this, map)
                } else {
                    //成员发起入群申请
                    map["content"] = "受邀入群"
                    viewModel.applyAddGroup(this, map)
                }
            } else {
                //创建群
                map["group_type"] = group_type.toString()
                viewModel.createGroup(this, map)
            }
        }
        bar.setTextView(select)
        bar.setOnTouchBarListener {
            viewModel.friendList
                    .forEachIndexed { index, friendBean ->
                        if (friendBean.pyname == it) {
                            mLayoutManager.scrollToPositionWithOffset(index, 1)
                            return@forEachIndexed
                        }
                    }
        }
    }

    override fun liveDataListener() {
        viewModel.friendLiveData
                .toObservable(this)
                {
                    viewModel.friendList.clear()
                    it.data?.list?.let {

                        it.forEach { item ->
                            item.pyname = PinyinUtils.getSurnameFirstLetter(item.mark)
                            if (!checkLetter(item.pyname)) {
                                item.pyname = "#"
                            }
                            item.canSelect = checkCanSelect(item)
                            viewModel.friendList.add(item)
                        }
                      val list=  viewModel.friendList.sortedBy {
                            it.pyname
                        }
                        viewModel.friendList.clear()
                        viewModel.friendList.addAll(list)
                    }
                    adapter.isInitEmpty = false
                    adapter.notifyDataSetChanged()
                }
        viewModel.createGroupLiveData
                .toObservable(this)
                {
                    if (it.data == null) {

                        showToast("创建成功")
                        finish()
                    } else {
                        val intent = Intent()
                        intent.putExtra("paybean", it.data)
                        setResult(400, intent)
                        finish()
                    }
                }

        viewModel.applyAddGroupLiveData
                .toObservable(this)
                {
                    showToast("邀请成功,等待审核")
                    setResult(400)
                    finish()
                }
        viewModel.joInToGroupLiveData
                .toObservable(this)
                {
                    showToast("邀请成功")
                    setResult(400)
                    finish()
                }
    }

    fun checkCanSelect(item: FriendBean): Boolean {
        return if (groupUserList != null) {
            groupUserList!!.toMutableList().none {
                it == item.uid
            }
        } else {
            true
        }
    }

    fun checkHasSelect(): Boolean {
        return viewModel.friendList
                .none {
                    it.select
                }
    }

    private fun checkLetter(str: String): Boolean {
        val c = str[0]
        return if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
            true
        } else {
            false
        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getFirendList(this)
    }
}