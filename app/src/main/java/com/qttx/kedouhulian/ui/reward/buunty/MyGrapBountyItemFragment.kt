package com.qttx.kedouhulian.ui.reward.buunty

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RefreshTaskFilter
import com.qttx.kedouhulian.ui.reward.buunty.adapter.MyGrapBountyAdapter
import com.qttx.kedouhulian.ui.reward.buunty.adapter.MyPublishBountyAdapter
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.MyGrapBountyViewModel
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.MyPublishBountyViewModel
import com.qttx.kedouhulian.utils.EventFilterBean
import com.qttx.kedouhulian.utils.EventStatus
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.utils.RxBus
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.widget.RecycleViewDivider
import io.rong.eventbus.EventBus
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * 我发布的红包
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyGrapBountyItemFragment : BaseFragment(), IListViewProvider {
    private var status: Int = 0

    companion object {
        fun getIntace(type: Int): MyGrapBountyItemFragment {
            val fragment = MyGrapBountyItemFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    val listModel: MyGrapBountyViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyGrapBountyAdapter

    private lateinit var layoutManger: LinearLayoutManager

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
//        EventBus.getDefault()
//                .register(this)
        RxBus
                .getDefault()
                .toObservable(RefreshTaskFilter::class.java)
                .subscribe{
                    if ("bounty_fired" == it.key||"bounty_hire"==it.key) {
                        ptrRequestListener(true)
                    }
                }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(filter: RefreshTaskFilter) {
//        if ("bounty_fired" == filter.key||"bounty_hire"==filter.key) {
//            ptrRequestListener(true)
//        }
//    }
    override fun onDestroyView() {
//        EventBus
//                .getDefault()
//                .unregister(this)
    RxBus
            .getDefault()
            .removeStickyEvent(RefreshTaskFilter::class.java)
        super.onDestroyView()
    }
    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = MyGrapBountyAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(appContext, BountyDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }
            onItemChildClickListener { bountyBean, i, view ->

                when (view.id) {
                    R.id.applyAcountTv -> {
                        listModel.applyFinish(this@MyGrapBountyItemFragment, bountyBean.id)
                    }
                    R.id.refuseTv -> {
                        listModel.applyConfirm(this@MyGrapBountyItemFragment, bountyBean.bounty_order_id, "0")
                    }
                    R.id.agreeTv -> {
                        listModel.applyConfirm(this@MyGrapBountyItemFragment, bountyBean.bounty_order_id, "1")
                    }
                }
            }
        }
    }

    override fun liveDataListener() {
        listModel.applyFinishLiveData.toObservable(this)
        {
            showToast("申请成功")
            ptrRequestListener(true)
        }
        listModel.confirmLiveData.toObservable(this)
        {
            ptrRequestListener(true)
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


}