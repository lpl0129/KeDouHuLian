package com.qttx.kedouhulian.ui.pond

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.pond.adapter.PondHisotyAdapter
import com.qttx.kedouhulian.ui.pond.viewModel.PondDetailViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import kotlinx.android.synthetic.main.pond_header_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.reward_task_activity_detail.*


class PondDetailActivity : BaseActivity(), IListViewProvider {

    private val listModel: PondDetailViewModel by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: PondHisotyAdapter

    private lateinit var layoutManger: LinearLayoutManager

    private lateinit var task_id: String

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        map["id"] = task_id
        listModel.getListData(this, isRefresh, map)
    }


    override fun getLayoutId(): Int = R.layout.reward_task_activity_detail

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL

        adapter = PondHisotyAdapter(listModel.list)
        adapter.setHeaderAndEmpty(true)
        adapter.addHeaderView(layoutInflater.inflate(R.layout.pond_header_detail, null))
        adapter.setListener {
            onbindHeaderData {
                listModel.detailLiveData
                        .value?.data?.apply {
                    buyPriceTv.text="${buy_price}元"
                    incomeZuJinTv.text="${income}元"
                    timeTv.text="占领时间：$change_time"
                    numberTv.text="易主次数：$trade_num"
                }
            }
        }
    }
    override fun liveDataListener() {

        listModel.detailLiveData.toObservable(this)
        {
            adapter.notifyDataSetChanged()
        }
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("详情")
        task_id = intent.getStringExtra("id")
        ptrRequestListener(true)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
    }

}
