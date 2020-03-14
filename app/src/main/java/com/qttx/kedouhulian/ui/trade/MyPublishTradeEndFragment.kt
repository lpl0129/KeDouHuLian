package com.qttx.kedouhulian.ui.trade

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.buunty.BountyDetailActivity
import com.qttx.kedouhulian.ui.trade.adapter.MyPublishTradeEndAdapter
import com.qttx.kedouhulian.ui.trade.viewModel.MyPublishTradeEndViewModel
import com.qttx.kedouhulian.ui.trade.viewModel.MyPublishTradeIngViewModel
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.dialog.TipDialog
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * 我发布的挂售中
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishTradeEndFragment : BaseFragment(), IListViewProvider {
    private var status: Int = 0

    companion object {
        fun getIntace(type: Int): MyPublishTradeEndFragment {
            val fragment = MyPublishTradeEndFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    val listModel: MyPublishTradeEndViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPublishTradeEndAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        listModel.getListData(this, isRefresh)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        status = arguments!!.getInt("status")
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = MyPublishTradeEndAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
//                val intent = Intent(appContext, BountyDetailActivity::class.java)
//                intent.putExtra("id", any.id.toString())
//                startActivityForResult(intent, 200)
            }
        }
    }


    override fun liveDataListener() {
        listModel.listManagerLiveData.toObservableList(this, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onShow()
    }

    override fun onShow() {
        ptrlayout.autoRefresh()
    }

}