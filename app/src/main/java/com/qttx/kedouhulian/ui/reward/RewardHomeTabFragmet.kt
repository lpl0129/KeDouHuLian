package com.qttx.kedouhulian.ui.reward

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.room.DataBase
import com.qttx.kedouhulian.ui.reward.msg.HomeMsghActivity
import com.qttx.kedouhulian.ui.reward.viewModel.RewardHomeViewModel
import com.qttx.kedouhulian.ui.reward.viewModel.SoceViewModel
import com.qttx.kedouhulian.ui.trade.TradeMarketActivity
import com.qttx.kedouhulian.ui.user.CopartnerActivity
import com.qttx.kedouhulian.ui.user.UserBonusActivity
import com.qttx.kedouhulian.ui.wallet.WalletActivity
import com.qttx.kedouhulian.utils.EventStatus
import com.qttx.kedouhulian.utils.EventUtils
import com.qttx.kedouhulian.utils.LocationHelper
import com.qttx.kedouhulian.weight.AntForestView
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.utils.StatusBarUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.reward_fragment_home_tab.*

import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.concurrent.TimeUnit


/**
 * @author huangyr
 * @date 2019/4/10 0010
 */

/**
 * 蝌蚪赏金首页
 */
class RewardHomeTabFragmet : BaseFragment() {


    var locationHelper: LocationHelper? = null
    lateinit var viewModel: RewardHomeViewModel

    val listModel: SoceViewModel  by viewModel()

    override fun getLayoutId(): Int {
//        val hasNav = StatusBarUtils.checkDeviceHasNavigationBar(appContext)
//        if (hasNav) {
//            return R.layout.reward_fragment_home_tab_min
//        } else {
            return R.layout.reward_fragment_home_tab
//        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden)
        {
            listModel.getListData(this)
        }
    }
    override fun onResume() {
        super.onResume()
        listModel.getUserInfo(this)
        listModel.getListData(this)
    }

    override fun processLogic() {

        requsetPerMission(10001, listOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
        yiJianShouQutv.setOnClickListener {
//            antView.clelarAll()
            listModel.clearSroce(this)
        }
        viewModel = ViewModelProviders.of(activity!!).get(RewardHomeViewModel::class.java)
        StatusBarUtils.addMarginTopEqualStatusBarHeight(topview)

    }

    override fun liveDataListener() {

        listModel.userinfo2LiveData.toObservable(this)
        {
            it.data?.let {
                myMoneyTv.text = "我的蝌蚪币：${it.score}"
                if (it.message_num == 0) {
                    msgUnRead.visibility = View.INVISIBLE
                } else {
                    msgUnRead.visibility = View.VISIBLE
                }

            }
        }
        listModel.clearLiveData.toObservable(this)
        {
            antView.clelarAll()
            emptyView.postDelayed({
                emptyView.visibility=View.VISIBLE
                antView.visibility=View.GONE
            },800)
            listModel.getUserInfo(this)
        }
        listModel.listLiveData.toObservable(this)
        {
            it.data?.list?.let {

                if (it.isEmpty())
                {
                    emptyView.visibility=View.VISIBLE
                    antView.visibility=View.GONE
                }else
                {
                    emptyView.visibility=View.GONE
                    antView.visibility=View.VISIBLE
                }

                antView.setViewDisappearLocation(intArrayOf(450, -200))
                antView.setData(it.filterIndexed { index, _ ->
                    index < 14
                })
                antView.setOnStopAnimateListener(object : AntForestView.OnStopAnimateListener {
                    override fun onBallDisappearAnimListener(number: String?) {
                    }

                    override fun onExitAnimateListener() {
                    }

                }
                )


            }
        }

        listModel.gongLvLiveData.toObservable(this)
        {
            it.data?.let {
                val bundle = Bundle()
                bundle.putString("url", it.content)
                val dilog = GuiZeDilog()
                dilog.arguments = bundle
                dilog.show(childFragmentManager)
            }

        }
    }

    @AfterPermissionGranted(10001)
    private fun getstartLocation() {
        locationHelper = LocationHelper(object : LocationHelper.LocationListener {
            override fun getLocationFailed() {

            }

            override fun getLocationSuccess(locationBean: LocationHelper.LocationBean?, aMapLocation: AMapLocation?) {
                locationHelper?.destroyLocation()
                locationHelper = null
                locationBean?.let {
                    Observable.just(it)
                            .subscribeOn(Schedulers.io())
                            .throttleFirst(5, TimeUnit.MINUTES)
                            .map {
                                val adcode = it.getAdcode()
                                val stree = DataBase.getInstance(context)
                                        .cityDao()
                                        .getCityByAdCode(adcode)
                                val city = DataBase.getInstance(context)
                                        .cityDao()
                                        .getCityByid(stree.pid)
                                val pro = DataBase.getInstance(context)
                                        .cityDao()
                                        .getCityByid(city.pid)
                                it.cityId = city.id
                                it.provinceId = pro.id
                                it.districtId = stree.id
                                it
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                viewModel.saveLocation(it)
                            }
                }
            }
        })
        locationHelper?.startLocation()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        val intent = Intent()
        when (v) {
            msgRl -> {
                val intent = Intent(appContext, HomeMsghActivity::class.java)
                startActivity(intent)
            }
            collectGongLueTv -> {
                val bundle = Bundle()
                bundle.putString("url", "")
                val dilog = GuiZeDilog()
                dilog.arguments = bundle
                dilog.show(childFragmentManager)
//                var text = ""
//                listModel.gongLvLiveData.value?.data?.let {
//                    text = it.content
//                }
//                if (text.isEmpty()) {
//                    listModel.getGongLv(this)
//                } else {
//                    val bundle = Bundle()
//                    bundle.putString("url", text)
//                    val dilog = GuiZeDilog()
//                    dilog.arguments = bundle
//                    dilog.show(childFragmentManager)
//                }

            }

            sjRenwuTv -> {
                intent.setClass(context, RewardHunterMainActivity::class.java)
                intent.putExtra("index", 0)
                startActivity(intent)
            }
            ggHongBaoTv -> {
//
                intent.setClass(context, RewardHunterMainActivity::class.java)
                intent.putExtra("index", 1)
                startActivity(intent)
            }
            sjLieRenTv -> {
                intent.setClass(context, RewardHunterMainActivity::class.java)
                intent.putExtra("index", 2)
                startActivity(intent)
            }
            kedouChiTangTv -> {
                EventUtils.postEvent(EventStatus.TAB_CHANGE, 2)
            }
            kdBiMaketTv -> {
                intent.setClass(context, TradeMarketActivity::class.java)
                startActivity(intent)

            }
            woDeQianBaoTv -> {
                intent.setClass(context, WalletActivity::class.java)
                startActivity(intent)
            }
            woDeHeHuoTv -> {
                intent.setClass(context, CopartnerActivity::class.java)
                startActivity(intent)
            }
            biFenHongTv -> {
                intent.setClass(context, UserBonusActivity::class.java)
                startActivity(intent)

            }
        }
    }

    override fun initViewClickListeners() {
        msgRl.setOnClickListener(this)
        collectGongLueTv.setOnClickListener(this)
        yiJianShouQutv.setOnClickListener(this)
        sjRenwuTv.setOnClickListener(this)
        ggHongBaoTv.setOnClickListener(this)
        sjLieRenTv.setOnClickListener(this)
        kedouChiTangTv.setOnClickListener(this)
        kdBiMaketTv.setOnClickListener(this)
        woDeQianBaoTv.setOnClickListener(this)
        woDeHeHuoTv.setOnClickListener(this)
        biFenHongTv.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationHelper?.destroyLocation()
        locationHelper = null
    }

//    fun goToTab(): Boolean {
//        if (getUserLocation() == null) {
//            TipDialog.newInstance("您当前未开启定位服务，为了方便您的使用，建议您开启定位服务"
//                    , "定位服务未开启", false, "去开启")
//                    .setListener {
//                        onSureClick {
//                            requsetPerMission(10001, listOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
//                        }
//                    }.show(childFragmentManager)
//            return false
//        } else {
//            return true
//        }
//    }
}
