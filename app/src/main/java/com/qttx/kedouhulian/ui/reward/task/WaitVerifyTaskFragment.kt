package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.amap.api.mapcore.util.it
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.adapter.MyPublishAdapter
import com.qttx.kedouhulian.ui.reward.task.adapter.WaitVerifyTaskAdapter
import com.qttx.kedouhulian.ui.reward.task.viewModel.MyPublishTaskViewModel
import com.qttx.kedouhulian.ui.reward.task.viewModel.WaitVerifyTaskViewModel
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
class WaitVerifyTaskFragment : BaseFragment(), IListViewProvider {


    companion object {
        fun getIntace(type: Int, string: String): WaitVerifyTaskFragment {
            val fragment = WaitVerifyTaskFragment()
            val bundle = Bundle()
            bundle.putInt("status", type)
            bundle.putString("taskid", string)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val listModel: WaitVerifyTaskViewModel  by viewModel()


    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    private lateinit var adapter: WaitVerifyTaskAdapter

    private lateinit var layoutManger: LinearLayoutManager

    private var status: Int = 0
    private var taskid: String = ""
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }


    override fun ptrRequestListener(isRefresh: Boolean) {
        val params = mutableMapOf<String, String>()
        params["status"] = status.toString()
        params["taskid"] = taskid
        listModel.getListData(this, isRefresh, params)
    }

    override fun getLayoutId(): Int = R.layout.common_ptr_list

    override fun processLogic() {
        status = arguments!!.getInt("status")
        taskid = arguments!!.getString("taskid")
    }

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = WaitVerifyTaskAdapter(listModel.list)
        adapter.setListener {
            onItemChildClickListener { taskBean, i, view ->

                if (taskBean.status == 1) {
                    val list = mutableListOf("通过", "不合格")
                    if (taskBean.amend_status == 0) {
                        list.add("待修改")
                    }
                    XPopup.Builder(appContext)
                            .asBottomList("审核结果", list.toTypedArray(), null, -1)
                            { position, text ->
                                run {
                                    listModel.ordrderid = taskBean.id
                                    when (text) {
                                        "通过" -> {
                                            listModel.status = 2
                                            listModel.sumitResult(this@WaitVerifyTaskFragment, "通过")
                                        }

                                        "不合格" -> {
                                            listModel.status = -1
                                            showEditTextDialog()
                                        }

                                        "待修改" -> {
                                            listModel.status = 3
                                            showEditTextDialog()
                                        }

                                    }

                                }
                            }.show()
                }
            }

        }
        listModel.listManagerLiveData.toObservableList(this, this)

        listModel.examineLiveData.toObservable(this)
        {
            showToast("提交成功")
            ptrlayout.autoRefresh()
        }
    }

    private var popupWindow: BasePopupView? = null
    private fun showEditTextDialog() {
        popupWindow = XPopup
                .Builder(appContext)
                .autoDismiss(false)
                .autoOpenSoftInput(true)
                .asInputConfirm("理由", "", "请您输入改用户什么地方未达标?以方便告知用户。")
                {
                    if (it.isNullOrEmpty()) {
                        showToast("请输入理由")
                    } else {
                        popupWindow?.dismiss()
                        listModel.sumitResult(this, it)
                    }
                }.show()
    }

    override fun onShow() {
        ptrlayout.autoRefresh()
    }
}