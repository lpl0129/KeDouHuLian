package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.FriendBean
import com.qttx.kedouhulian.ui.chat.adapter.ChatGroupMemberAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.ui.user.UserDataActivity
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import kotlinx.android.synthetic.main.common_list.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
/**
 * 全部群成员
 */
class ChatGroupMemberActivity : BaseActivity() {

    private val viewModel: ChatViewModel by viewModel()
    private val list = mutableListOf<FriendBean>()

    private var groupid = ""
    private var isOwner = false

    private var intpositon = 0

    private lateinit var adapter: ChatGroupMemberAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    override fun getLayoutId(): Int {
        return R.layout.common_list
    }

    override fun initBeforeListener() {
        super.initBeforeListener()

        adapter = ChatGroupMemberAdapter(list)
        adapter.isOwner = isOwner

        adapter.setListener {
            onItemClickListener { friendBean, i, view ->
                //用户主页
                UserDataActivity.show(this@ChatGroupMemberActivity,friendBean.uid)
            }
            onItemChildClickListener { friendBean, i, view ->
                intpositon = i

                viewModel.delMemberFromGroup(this@ChatGroupMemberActivity, groupid, uid = friendBean.uid)
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
        setTopTitle("全部成员")
        groupid = intent.getStringExtra("id")
        isOwner = intent.getBooleanExtra("isOwner", false)
        viewModel.getGroupInfoById(this, groupid, ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun liveDataListener() {
        viewModel.grioupInfoLiveData
                .toObservable(this)
                {
                    list.clear()
                    it.data?.let {
                        list.addAll(it.list)
                    }
                    adapter.notifyDataSetChanged()
                }
        viewModel.delMemberFromGroupLiveData.toObservable(this)
        {
            showToast("剔出成功")
            list.removeAt(intpositon)
            setResult(400)
            adapter.notifyDataSetChanged()
        }
    }


    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getGroupInfoById(this, groupid, ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    }
}