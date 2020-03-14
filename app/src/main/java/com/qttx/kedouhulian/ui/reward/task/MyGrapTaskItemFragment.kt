package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.adapter.MyGrapTaskAdapter
import com.qttx.kedouhulian.ui.reward.task.viewModel.MyGrapTaskViewModel
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
class MyGrapTaskItemFragment : BaseFragment(), IListViewProvider {


    companion object {
        fun getIntace(type: Int): MyGrapTaskItemFragment {
            val fragment = MyGrapTaskItemFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val listModel: MyGrapTaskViewModel  by viewModel()


    private var status: Int = 0

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["status"] = status.toString()
        listModel.getListData(this, isRefresh, map)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        status = arguments!!.getInt("status")
    }

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = MyGrapTaskAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { myGrapTaskBean, i, view ->
                val intent = Intent(appContext, TaskOrderDetailActivity::class.java)
                intent.putExtra("id", myGrapTaskBean.task_id.toString())
                intent.putExtra("orderid", myGrapTaskBean.id.toString())

                startActivityForResult(intent, 200)
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onShow()
    }

    override fun onShow() {
        ptrlayout.autoRefresh()
    }

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyGrapTaskAdapter

    private lateinit var layoutManger: LinearLayoutManager
}