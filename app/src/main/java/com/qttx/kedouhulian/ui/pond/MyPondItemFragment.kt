package com.qttx.kedouhulian.ui.pond

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.ui.pond.adapter.MyPondAdapter
import com.qttx.kedouhulian.ui.pond.viewModel.MyPondViewModel
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.pond_fragment_home_tab.*
import kotlinx.android.synthetic.main.reward_fragment_task_grap.*
import kotlinx.android.synthetic.main.reward_fragment_task_grap.ptrlayout
import kotlinx.android.synthetic.main.reward_fragment_task_grap.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/19 0019
 */
class MyPondItemFragment : BaseFragment(), IListViewProvider {


    private val listModel: MyPondViewModel  by viewModel()


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }


    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["type"] = "0"
        listModel.getListData(this, isRefresh, map)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        ptrRequestListener(true)
    }

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = MyPondAdapter(listModel.list)
        adapter.setHeaderAndEmpty(true)
        val view = layoutInflater.inflate(R.layout.pond_header_my, null)
        view.setOnClickListener {
            MineNotifyWebActivity.show(appContext, "塘主特权", BuildConfig.BaseUrl + "Wxview/tangzhuprivilege")
        }
        adapter.addHeaderView(view)
        adapter.setListener {
            onItemClickListener { pondBean, i, view ->

                val intent = Intent(appContext, PondDetailActivity::class.java)
                intent.putExtra("id", pondBean.id.toString())
                startActivityForResult(intent, 200)
            }
            onItemChildClickListener { myGrapTaskBean, i, view ->
                if (myGrapTaskBean.type == 0) {
                    showEditTextDialog(myGrapTaskBean.id.toString())
                } else {
                    val map = mutableMapOf<String, String>()
                    map["id"] = myGrapTaskBean.id.toString()
                    map["type"] = "2"
                    listModel.poolUpdateType(this@MyPondItemFragment, map)
                }
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)
        listModel.updateLiveData.toObservable(this)
        {
            ptrRequestListener(true)
        }
    }

    private var popupWindow: BasePopupView? = null
    private fun showEditTextDialog(id: String) {
        popupWindow = XPopup
                .Builder(appContext)
                .autoDismiss(false)
                .autoOpenSoftInput(true)
                .asInputConfirm("定价转让", "", "请输入转让价格")
                {
                    if (it.isNullOrEmpty()) {
                        showToast("请输入价格")
                    } else {
                        popupWindow?.dismiss()
                        val map = mutableMapOf<String, String>()
                        map["id"] = id
                        map["type"] = "1"
                        map["price"] = it
                        listModel.poolUpdateType(this, map)
                    }
                }.show()
    }

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: MyPondAdapter

    private lateinit var layoutManger: LinearLayoutManager
}