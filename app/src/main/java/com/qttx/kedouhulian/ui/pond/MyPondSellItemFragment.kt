package com.qttx.kedouhulian.ui.pond

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.pond.adapter.MyPondAdapter
import com.qttx.kedouhulian.ui.pond.adapter.MyPondSellAdapter
import com.qttx.kedouhulian.ui.pond.viewModel.MyPondViewModel
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
class MyPondSellItemFragment : BaseFragment(), IListViewProvider {

    companion object {
        fun getIntace(type: Int): MyPondSellItemFragment {
            val fragment = MyPondSellItemFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val listModel: MyPondViewModel  by viewModel()


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["type"] = "1"
        listModel.getListData(this, isRefresh, map)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        ptrRequestListener(true)
    }

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = MyPondSellAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { pondBean, i, view ->

                val intent = Intent(appContext, PondDetailActivity::class.java)
                intent.putExtra("id", pondBean.pool_id.toString())
                startActivityForResult(intent, 200)
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)
    }


    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPondSellAdapter

    private lateinit var layoutManger: LinearLayoutManager
}