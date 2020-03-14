package com.qttx.kedouhulian.ui.reward.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.ScoreBean
import com.qttx.kedouhulian.net.NetLiveData
import com.qttx.kedouhulian.utils.LocationHelper
import com.qttx.kedouhulian.utils.Utils
import com.qttx.kedouhulian.utils.saveUserLocation
import com.stay.toolslibrary.net.BaseViewModel

/**
 * @author huangyr
 * @date 2019/4/15 0015
 */
class RewardHomeViewModel : BaseViewModel() {

    val locationLiveData = MutableLiveData<LocationHelper.LocationBean>()

    fun saveLocation(bean: LocationHelper.LocationBean) {
        locationLiveData.value = bean
        saveUserLocation(bean)
    }

}