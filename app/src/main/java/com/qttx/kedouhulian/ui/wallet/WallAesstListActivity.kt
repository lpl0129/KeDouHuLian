package com.qttx.kedouhulian.ui.wallet

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.wallet.adapter.WallAesstListAdapter
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletListViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import kotlinx.android.synthetic.main.chat_activity_apply_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * 我的资金明细
 */
class WallAesstListActivity : BaseActivity(), IListViewProvider {

    private val listModel: WalletListViewModel by viewModel()
    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: WallAesstListAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.chat_activity_grouplist
    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = WallAesstListAdapter(listModel.list)
    }

    override fun liveDataListener() {
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("资金明细")
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
        super.onResume()
    }
}
