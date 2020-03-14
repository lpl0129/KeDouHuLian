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
import com.qttx.kedouhulian.ui.trade.adapter.MyPublishTradeIngAdapter
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
class MyPublishTradeIngFragment : BaseFragment(), IListViewProvider {
    private var status: Int = 0

    companion object {
        fun getIntace(type: Int): MyPublishTradeIngFragment {
            val fragment = MyPublishTradeIngFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    val listModel: MyPublishTradeIngViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPublishTradeIngAdapter

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
        listModel.getListData(this, isRefresh)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        status = arguments!!.getInt("status")
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = MyPublishTradeIngAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
//                val intent = Intent(appContext, BountyDetailActivity::class.java)
//                intent.putExtra("id", any.id.toString())
//                startActivityForResult(intent, 200)
            }
            onItemChildClickListener { bountyBean, i, view ->

                when (view.id) {
                    R.id.editPriceTv -> {
                        showEditTextDialog(bountyBean.id.toString())
                    }
                    R.id.cancleTv -> {
                        TipDialog
                                .newInstance("确认撤销改订单?", "提示", false)
                                .setListener {
                                    onSureClick {
                                        listModel.applyCancel(this@MyPublishTradeIngFragment, bountyBean.id.toString())
                                    }

                                }
                                .show(childFragmentManager)
                    }
                }
            }
        }
    }

    private var popupWindow: BasePopupView? = null
    private fun showEditTextDialog(id: String) {
        popupWindow = XPopup
                .Builder(appContext)
                .autoDismiss(false)
                .autoOpenSoftInput(true)
                .asInputConfirm("修改价格", "", "请输入修改后的价格(元)")
                {
                    if (it.isNullOrEmpty()) {
                        showToast("请输入价格")
                    } else {
                        popupWindow?.dismiss()
                        listModel.update_price(this, id, it)
                    }
                }.show()
    }

    override fun liveDataListener() {
        listModel.applyFinishLiveData.toObservable(this)
        {
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