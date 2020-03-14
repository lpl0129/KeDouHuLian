package com.stay.toolslibrary.net

import android.arch.lifecycle.ViewModel
import com.stay.toolslibrary.utils.ToastUtils
import org.koin.standalone.KoinComponent

/**
 * @author huangyr
 * @date 2019/4/2 0002
 */
open class BaseViewModel : ViewModel(), KoinComponent {
    fun showToast(text: String) {
        ToastUtils.showShort(text)
    }
}