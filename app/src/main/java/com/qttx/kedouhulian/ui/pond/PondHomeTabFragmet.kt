package com.qttx.kedouhulian.ui.pond

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.amap.api.mapcore.util.it
import com.githang.statusbar.StatusBarCompat
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.common.SettingActivity
import com.qttx.kedouhulian.ui.dialog.ChoseCityDilog
import com.qttx.kedouhulian.ui.dialog.PondDialog
import com.qttx.kedouhulian.ui.dialog.PublishDialog
import com.qttx.kedouhulian.ui.pond.adapter.PondHomeAdapter
import com.qttx.kedouhulian.ui.pond.viewModel.PondHomeViewModel
import com.qttx.kedouhulian.ui.reward.redpacket.RedPacketDetailActivity
import com.qttx.kedouhulian.ui.reward.task.MyTaskActivity
import com.qttx.kedouhulian.ui.reward.task.TaskPublishActivity
import com.qttx.kedouhulian.utils.getUserLocation
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.RequestListenerBuilder
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.ScreenUtils
import com.stay.toolslibrary.utils.StatusBarUtils
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.utils.extension.px2dp
import com.stay.toolslibrary.widget.RecycleViewDivider
import com.stay.toolslibrary.widget.dialog.TipDialog
import kotlinx.android.synthetic.main.pond_fragment_home_tab.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * @author huangyr
 * @date 2019/4/10 0010
 */

/**
 * 蝌蚪赏金首页
 */
class PondHomeTabFragmet : BaseFragment(), IListViewProvider {

    private val listViewModel: PondHomeViewModel by viewModel()

    override fun ptrRequestListener(isRefresh: Boolean) {
        if (isRefresh) {
            listViewModel.pondHomeBean(this)
        }
        val params = mutableMapOf<String, String>()
        val city = listViewModel.getNowCity()
        city?.let {
            when {
                it.level == 1 -> params["province"] = it.id.toString()
                it.level == 2 -> params["city"] = it.id.toString()
                it.level == 3 -> params["district"] = it.id.toString()
            }
        }
        params["type"] = listViewModel.type.toString()
        listViewModel.getListData(this, isRefresh, params)
    }


    override fun getLayoutId(): Int = R.layout.pond_fragment_home_tab

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
//        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = PondHomeAdapter(listViewModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(appContext, PondDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }
            onItemChildClickListener { poolBean, i, view ->
                TipDialog.newInstance("是否确认购买?", "提示")
                        .setListener {
                            onSureClick {
                                listViewModel.buyPond(this@PondHomeTabFragmet, poolBean.id.toString(), poolBean.uid.toString())
                            }
                        }.show(childFragmentManager)
            }
        }
        listViewModel.listManagerLiveData.toObservableList(this, this)
        listViewModel.buyPondLiveData.toObservable(this)
        {
            it.data?.let {
                PayActivity.newInstance(it)
                        .setListener(PayResult {
                            ptrRequestListener(true)
                        })
                        .show(childFragmentManager)
            }

        }
        listViewModel.pondIncomeLiveData.toObservable(this)
        {
            it.data?.let {
                allIncomeTv.text = "¥  ${it.sum}"
                if (it.yday >= 0) {
                    lastDayIncomeTv.text = "昨日：+¥${it.yday}"
                } else {
                    lastDayIncomeTv.text = "昨日：-¥${Math.abs(it.yday)}"
                }

            }
        }
    }

    override fun processLogic() {

        tequanTv.setOnClickListener {
            MineNotifyWebActivity.show(appContext, "塘主特权", BuildConfig.BaseUrl + "Wxview/tangzhuprivilege")
        }
        collapsing_toolbar.minimumHeight = StatusBarUtils.getStatusBarHeight() + 101.dp2px()
        initViewData()
        whenScollChangeView()
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            listViewModel.setTopHeight(p1)
            whenScollChangeView()
        }
        )
        tabLl1.setOnClickListener {
            if (listViewModel.type == 1) {
                listViewModel.type = 0
                initViewData()
                ptrRequestListener(true)
            }
        }
        tabLl2.setOnClickListener {
            if (listViewModel.type == 0) {
                listViewModel.type = 1
                initViewData()
                ptrRequestListener(true)
            }
        }
        regionLimitLl.setOnClickListener {
            ChoseCityDilog
                    .newInstance(listViewModel.screenHeight, listViewModel.getNowCityList(), listViewModel.getNowCity(), -1, 1)
                    .setStateListenerListener {

                    }
                    .setSelectListener { list, bean ->
                        listViewModel.getNowCityList().clear()
                        listViewModel.getNowCityList().addAll(list)
                        listViewModel.setNowCity(bean)
                        initLimitCityData()
                        ptrRequestListener(true)
                    }.show(childFragmentManager)
        }
        floatBt.setOnClickListener {
            val dialog = PondDialog(appContext)
                    .setOffsetXAndY(0, 0)
                    .setOnSelectListener(OnSelectListener { position, text ->
                        if (position == 1) {
                            val intent = Intent(appContext, MyPondActivity::class.java)
                            startActivityForResult(intent, 200)
                        } else if (position == 2) {
                            SettingActivity.show(appContext, "poll")
                        }
                    }
                    )
            XPopup.Builder(appContext)
                    .hasShadowBg(false)
                    .atView(floatBt)
                    .asCustom(dialog)
                    .show()

        }
        ptrRequestListener(true)

    }

    private fun initViewData() {
        if (listViewModel.isWhiteState) {
            tabTv1.setTextColor(Color.parseColor(if (listViewModel.type == 0) "#FF333333" else "#FF858585"))
            tabTv2.setTextColor(Color.parseColor(if (listViewModel.type == 1) "#FF333333" else "#FF858585"))
            tabSpiteView1.setBackgroundColor(resources.getColor(R.color.deepColor))
            tabSpiteView2.setBackgroundColor(resources.getColor(R.color.deepColor))
        } else {
            tabTv1.setTextColor(Color.parseColor(if (listViewModel.type == 0) "#FFFFFFFF" else "#FFB0C0F5"))
            tabTv2.setTextColor(Color.parseColor(if (listViewModel.type == 1) "#FFFFFFFF" else "#FFB0C0F5"))

            tabSpiteView1.setBackgroundColor(resources.getColor(R.color.whiteColor))
            tabSpiteView2.setBackgroundColor(resources.getColor(R.color.whiteColor))
        }
        tabSpiteView1.visibility = if (listViewModel.type == 0) View.VISIBLE else View.INVISIBLE
        tabSpiteView2.visibility = if (listViewModel.type == 1) View.VISIBLE else View.INVISIBLE
        initLimitCityData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200) {
            ptrRequestListener(true)
        }
    }

    private fun initLimitCityData() {
        if (listViewModel.getNowCityList().isEmpty()) {
            regionLimitTv.text = ""
        } else {
            val startbuilder = StringBuilder()
            listViewModel.getNowCityList().forEach {
                if (it.id != 0) {
                    startbuilder.append(it.name)
                            .append(",")
                }
            }
            if (TextUtils.isEmpty(startbuilder)) {
                regionLimitTv.text = ""
            } else {
                regionLimitTv.text = startbuilder.deleteCharAt(startbuilder.length - 1).toString()
            }
        }
    }

    private fun whenScollChangeView() {
        if (listViewModel.isWhiteState && placeholderView.visibility == View.GONE) {
            //到达顶部
            placeholderView.visibility = View.VISIBLE
            user_detail_bkg_img.visibility = View.GONE
            areaParentLl.setBackgroundColor(resources.getColor(R.color.whiteColor))
            bottomSpiteLine.visibility = View.VISIBLE
            initViewData()
            StatusBarCompat.setStatusBarColor(activity, resources.getColor(com.stay.basiclib.R.color.whiteColor))
        } else if (!listViewModel.isWhiteState && placeholderView.visibility == View.VISIBLE) {
            //移出顶部
            placeholderView.visibility = View.GONE
            user_detail_bkg_img.visibility = View.VISIBLE
            areaParentLl.setBackgroundColor(resources.getColor(R.color.translucent))
            bottomSpiteLine.visibility = View.GONE
            initViewData()
            StatusBarCompat.setStatusBarColor(activity, resources.getColor(com.stay.basiclib.R.color.translucent))
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (listViewModel.isWhiteState) {
                StatusBarCompat.setStatusBarColor(activity, resources.getColor(com.stay.basiclib.R.color.whiteColor))
            } else {
                StatusBarCompat.setStatusBarColor(activity, resources.getColor(com.stay.basiclib.R.color.translucent))
            }
        }
    }


    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: PondHomeAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun getListenerBuilder(): RequestListenerBuilder {
        return RequestListenerBuilder().apply {
            checkCanRefresh {
                listViewModel.isTop
            }
            onRequestSuccess {
                if (it) {
                    layoutManger.scrollToPositionWithOffset(0, 0)
                }
            }
        }

    }
}
