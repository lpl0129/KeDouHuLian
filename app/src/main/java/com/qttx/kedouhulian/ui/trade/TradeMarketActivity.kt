package com.qttx.kedouhulian.ui.trade

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.SettingActivity
import com.qttx.kedouhulian.ui.dialog.TradMarketDialog
import com.qttx.kedouhulian.ui.reward.redpacket.MyRedPacketActivity
import com.qttx.kedouhulian.ui.reward.redpacket.RedPacketPublishActivity
import com.qttx.kedouhulian.ui.trade.adapter.TradeMarketAdapter
import com.qttx.kedouhulian.ui.trade.viewModel.TradeMarketViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.trade_activity_market.*
import kotlinx.android.synthetic.main.trade_activity_market.floatBt
import kotlinx.android.synthetic.main.trade_activity_market.ptrlayout
import kotlinx.android.synthetic.main.trade_activity_market.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/8 0008
 */
class TradeMarketActivity : BaseActivity(), IListViewProvider {

    val listViewModel: TradeMarketViewModel by viewModel()
    private lateinit var adapter: TradeMarketAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int {
        return R.layout.trade_activity_market
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = TradeMarketAdapter(listViewModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(this@TradeMarketActivity, TradeDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }

        }
    }

    override fun liveDataListener() {
        listViewModel.resultLiveData.toObservable(this)
        {
            it.data?.let {
                mySocreTv.text = "我的蝌蚪币数量：${it.all_score}枚"
                availableSocreTv.text = "可用于交易数量：${it.score}枚"
                tradeTimeTv.text = "${it.start_time}-${it.end_time}"
            }
        }
        listViewModel.listManagerLiveData.toObservableList(this, this)
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("蝌蚪币交易市场")
        ptrRequestListener(true)
        floatBt.setOnClickListener {
            val dialog = TradMarketDialog(this)
                    .setOffsetXAndY(-4000, 0)
                    .setOnSelectListener(OnSelectListener { position, text ->
                        if (position == 0) {
                            val intent = Intent(this, TradePublishActivity::class.java)
                            startActivityForResult(intent, 200)
                        } else if (position == 1) {
                            val intent = Intent(this, MyTradeActivity::class.java)
                            startActivityForResult(intent, 200)
                        }else if (position == 2) {
                            SettingActivity.show(this,"trade")
                        }
                    }
                    )
            XPopup.Builder(this)
                    .hasShadowBg(false)
                    .atView(floatBt)
                    .asCustom(dialog)
                    .show()

        }
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        listViewModel.getListData(this, isRefresh)
    }

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout

    override val recyclerViewProvider: RecyclerView
        get() = recyclerView


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 400) {
            ptrRequestListener(true)
        }
    }

}