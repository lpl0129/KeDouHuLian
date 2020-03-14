package com.qttx.kedouhulian.ui.chat

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.ChatGroupAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatGroupViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import io.rong.imkit.RongIM
import io.rong.imlib.model.Group
import kotlinx.android.synthetic.main.chat_activity_apply_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * 群信息, 和群成员信息的刷新机制
 */
class ChatGroupListActivity : BaseActivity(), IListViewProvider {

    private val listModel: ChatGroupViewModel by viewModel ()

    private var id: String = ""
    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: ChatGroupAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.chat_activity_grouplist
    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = ChatGroupAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { groupListBean, i, view ->
                RongIM.getInstance().refreshGroupInfoCache(Group(groupListBean.group_id, groupListBean.group_name, Uri.parse(groupListBean.group_avatar)))
                RongIM.getInstance().startGroupChat(this@ChatGroupListActivity,
                        groupListBean.group_id, groupListBean.group_name)
            }

        }
    }

    override fun liveDataListener() {
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("群聊")
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

    override fun onResume() {
        ptrRequestListener(true)
        super.onResume()
    }
}
