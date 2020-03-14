package com.qttx.kedouhulian.ui.pond.viewModel

import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.BaseListViewModel
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.ScreenUtils
import com.stay.toolslibrary.utils.StatusBarUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.utils.extension.px2dp
import io.reactivex.Observable

/**
 * @author huangyr
 * @date 2019/5/6 0006
 */
class PondHomeViewModel(val api: Api) : BaseListViewModel<PondBean>() {

    private var topHeight = 166.dp2px()
    val screenHeight = (ScreenUtils.getScreenHeight() - StatusBarUtils.getStatusBarHeight()).px2dp().toInt()

    /**
     * 是否的顶部
     */
    var isTop = true
        private set
    /**
     * 是否是白色状态
     */
    var isWhiteState = false
        private set

    /**
     * 类型
     */
    var type = 0

    /**
     * 当前高度
     */
    private var nowHeight = 0

    private val startList0 = ArrayList<RegionsBean>()

    private var startParentCity0: RegionsBean? = null

    private val startList1 = ArrayList<RegionsBean>()

    private var startParentCity1: RegionsBean? = null

    val buyPondLiveData = NetLiveData<PayBean>()

    val pondIncomeLiveData = NetLiveData<PondIncomeBean>( ViewLoadingStatus.LOADING_NO)

    fun setTopHeight(height: Int) {
        nowHeight = Math.abs(height)
        isTop = nowHeight == 0
        isWhiteState = nowHeight >= topHeight
        if (isWhiteState) {
            LogUtils.e(topHeight)
            LogUtils.e(nowHeight)
        }
    }

    fun getNowCity(): RegionsBean? {
        return if (type == 0) startParentCity0 else startParentCity1
    }

    fun setNowCity(bean: RegionsBean?) {
        if (type == 0) {
            startParentCity0 = bean
        } else {
            startParentCity1 = bean
        }
    }

    fun getNowCityList(): ArrayList<RegionsBean> {
        return if (type == 0) startList0 else startList1
    }

    override fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondBean>>> {

        return api.poolList(params)
    }

    fun buyPond(owner: LifecycleOwner, id: String, ownuserid: String) {
        val map = mutableMapOf<String, String>()
        map["id"] = id
        map["owner"] = ownuserid
        api.buyPool(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(buyPondLiveData)
    }

    fun pondHomeBean(owner: LifecycleOwner) {

        api.rateAcc()
                .bindSchedulerExceptionLife(owner)
                .subscribe(pondIncomeLiveData)
    }

}