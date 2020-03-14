package com.qttx.kedouhulian.net

import com.qttx.kedouhulian.bean.NetResultBean
import com.stay.toolslibrary.net.BaseVMObserver
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus

/**
 * @author huangyr
 * @date 2019/2/12 0012
 */
open class NetObserver<T> (loadingStatus: ViewLoadingStatus = ViewLoadingStatus.LOADING_DIALOG,
                     errorStatus: ViewErrorStatus = ViewErrorStatus.ERROR_TOAST,
                     loadingText: String = "加载中...",
                     block: NetResultBean<T>.() -> Unit)
    : BaseVMObserver<NetResultBean<T>>(loadingStatus, errorStatus, loadingText, block) {

    override fun getInstanceClass(): NetResultBean<T> {
        return NetResultBean()
    }

}
