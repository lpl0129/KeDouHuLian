package com.qttx.kedouhulian.net

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.bean.NetResultBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.stay.toolslibrary.base.BaseApplication
import com.stay.toolslibrary.net.*
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import io.reactivex.Observable
import io.rong.imlib.statistics.UserData.name

/**
 * @author huangyr
 * @date 2019/2/12 0012
 */
abstract class BaseListViewModel<T> : BaseViewModel() {

    var netRequestConfig: NetRequestConfig = BaseApplication.getApplication().requestConfig

    var pageInitial: Int = netRequestConfig.pageInitial

    /**
     * 页码
     */
    var page: Int = pageInitial
        private set
    /**
     * 一页多少数据
     */
    var pageSize: Int = netRequestConfig.pageSizeInitial

    var pageKey = netRequestConfig.pageKey

    var pageSizeKey = netRequestConfig.pageSizeKey
    /**
     * 请求总数据结果监听
     */
    var resultLiveData: SingleLiveEvent<NetResultBean<NetResultListBean<T>>> = SingleLiveEvent()

    /**
     * list结果监听
     */
    var listManagerLiveData: SingleLiveEvent<NetListManager> = SingleLiveEvent()

    /**
     * list结果集
     */
    var list = mutableListOf<T>()


    private var observer: NetObserver<NetResultListBean<T>> = NetObserver()
    {
        //结果值监听

        val msgBean = getNetMsgBean()

        val status: ViewStatus = msgBean.status
        val isRefresh = page == pageInitial

        when {
            status.isLoadingStatus() -> {
                //请求前的监听方法前置到请求时,不通过回调通知处理
//                val isEmpty = list.isEmpty()
//                val netListManaer = NetListManager(NetListListenerStatus.REQUEST_BEGIN, msgBean, isRefresh, true, isEmpty)
//                listManagerLiveData.value = netListManaer
            }
            status.isErrorStatus() -> {

                val isEmpty = list.isEmpty()
                if (!isRefresh) {
                    page--
                }
                val netListManaer = NetListManager(NetListListenerStatus.REQUEST_FAILED, msgBean, isRefresh, true, isEmpty)
                listManagerLiveData.value = netListManaer

            }
            status.isSuccessStatus() -> {
                resultLiveData.value = this
                val resultlist = data?.list
                var hasMore = false
                /**
                 *当前请求返回的数据是否是空,
                 * 非数据集合是否是空
                 */
                var isEmpty = true
                resultlist?.let {

                    hasMore = it.size >= pageSize
                    isEmpty = it.isEmpty()
                    if (isRefresh) {
                        list.clear()
                    } else if (isEmpty) {
                        page--
                    }
                    list.addAll(it)
                }
                //发送结果响应
                val netListManaer = NetListManager(NetListListenerStatus.REQUEST_SUCCESS, msgBean, isRefresh, hasMore, isEmpty)
                listManagerLiveData.value = netListManaer
            }
        }
    }
    var params = mutableMapOf<String, String>()

    open fun getListData(own: LifecycleOwner, isRefresh: Boolean = true, requestParams: Map<String, String>? = null
    ) {
        params.clear()
        if (isRefresh) {
            page = pageInitial
        } else {
            page++
        }
        params[pageKey] = page.toString()
        params[pageSizeKey] = pageSize.toString()
        requestParams?.let { map ->
            map.forEach { item ->
                params[item.key] = item.value
            }
        }
        val observableNew = getObservable(params)
        observableNew.bindSchedulerExceptionLife(own).subscribe(observer)
    }

    abstract fun getObservable(params: Map<String, String>): Observable<NetResultBean<NetResultListBean<T>>>

}