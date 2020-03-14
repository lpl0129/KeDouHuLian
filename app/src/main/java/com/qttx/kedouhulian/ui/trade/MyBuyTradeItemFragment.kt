package com.qttx.kedouhulian.ui.trade

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.trade.adapter.MyPublishBuyAdapter
import com.qttx.kedouhulian.ui.trade.viewModel.MyBuyTradeViewModel
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyBuyTradeItemFragment : BaseFragment(), IListViewProvider {
    private var status: Int = 0

    companion object {
        fun getIntace(type: Int): MyBuyTradeItemFragment {
            val fragment = MyBuyTradeItemFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    val listModel: MyBuyTradeViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPublishBuyAdapter

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
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = MyPublishBuyAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                //                val intent = Intent(appContext, BountyDetailActivity::class.java)
//                intent.putExtra("id", any.id.toString())
//                startActivityForResult(intent, 200)
            }
            onItemChildClickListener { tradeMyBuy, i, view ->

                when (view.id) {
                    R.id.pay_tv -> {
                        val payBean = PayBean(data_id = tradeMyBuy.id, fromtype = tradeMyBuy.fromtype, paymoney = tradeMyBuy.amount.toDouble())
                        PayActivity.newInstance(payBean)
                                .setListener(PayResult {
                                    ptrRequestListener(true)
                                }).show(childFragmentManager)
                    }
                    R.id.cancletv -> {
                        listModel.applyCancel(this@MyBuyTradeItemFragment, tradeMyBuy.id)
                    }
                }
            }
        }
    }


    override fun liveDataListener() {
        listModel.applyFinishLiveData.toObservable(this)
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