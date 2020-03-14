package com.qttx.kedouhulian.ui.reward.redpacket

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.redpacket.adapter.MyPublishRedPacketAdapter
import com.qttx.kedouhulian.ui.reward.redpacket.viewModel.MyPublishRedPacketViewModel
import com.qttx.kedouhulian.ui.reward.task.MyGrapTaskItemFragment
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * 我发布的红包
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyPublishRedPacketFragment : BaseFragment(), IListViewProvider {

    private var status: Int = 0
    companion object {
        fun getIntace(type: Int): MyPublishRedPacketFragment {
            val fragment = MyPublishRedPacketFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }


    val listModel: MyPublishRedPacketViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPublishRedPacketAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener( isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["status"] = status.toString()
        listModel.getListData(this, isRefresh,map)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {

        status = arguments!!.getInt("status")
        ptrlayout.autoRefresh()
    }

    override fun liveDataListener() {

        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = MyPublishRedPacketAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(appContext, RedPacketDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==200)
        {
            ptrlayout.autoRefresh()
        }
    }

}