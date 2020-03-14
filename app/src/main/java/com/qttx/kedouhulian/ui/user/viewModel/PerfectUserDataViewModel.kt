package com.qttx.kedouhulian.ui.user.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.AgeLevelBean
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife

class PerfectUserDataViewModel constructor(val api: Api) : BaseViewModel() {

    var ageListData = NetLiveData<List<AgeLevelBean>>()

    var saveAgeData = NetLiveData<Any>()
    var id: String? = null

    var sex: Int? = null

    var province: Int? = null
    var city: Int? = null
    var district: Int? = null

    fun getAge(own: LifecycleOwner) {
        api.age_group()
                .bindSchedulerExceptionLife(own)
                .subscribe(ageListData)
    }

    fun setAet(own: LifecycleOwner) {
        val map = mutableMapOf<String, String>()
        if (id == null) {
            showToast("请选择年龄段")
            return
        }
        if (sex == null) {
            showToast("请选择性别")
            return
        }
        if (district == null) {
            showToast("请选择常驻区域")
            return
        }

        map["agegruopID"] = id!!
        map["gender"] = sex.toString()
        map["province"] = province.toString()
        if (city != null) {
            map["city"] = city.toString()
        }
        map["district"] = district.toString()
        api.perfect_info(map)
                .bindSchedulerExceptionLife(own)
                .subscribe(saveAgeData)
    }

}
