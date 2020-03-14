package com.stay.toolslibrary.net

import android.support.v7.widget.RecyclerView

import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.utils.LogUtils

/**
 * Created by huang on 2017/7/18.
 */

interface IListViewProvider : IListViewLisenerProvider {

    fun ptrRequestListener(isRefresh: Boolean = true)

    val ptrProvider: PtrFrameLayout

    val recyclerViewProvider: RecyclerView

    fun getLayoutManager(): RecyclerView.LayoutManager

    fun getRecyclerAdapter(): RecyclerAdapter<*>


}

interface IListViewLisenerProvider {
    //注入方法
//    fun getListenerBuilder(): (RequestListenerBuilder.() -> Unit)?

    fun getListenerBuilder(): RequestListenerBuilder?
}


class RequestListenerBuilder {
    internal var mcheckCanRefreshAction: (() -> Boolean)? = null

    /**
     * 参数为 是否是刷新请求
     * isRefresh
     */
    internal var mrequestBeaginAction: ((Boolean) -> Unit)? = null
    internal var mrequestSuccessAction: ((Boolean) -> Unit)? = null
    internal var mrequestFailedAction: ((Boolean) -> Unit)? = null

    fun checkCanRefresh(action: () -> Boolean) {
        mcheckCanRefreshAction = action
    }

    fun onRequestBegin(action: (Boolean) -> Unit) {
        mrequestBeaginAction = action
    }

    fun onRequestSuccess(action: (Boolean) -> Unit) {
        mrequestSuccessAction = action
    }

    fun onRequestFailed(action: (Boolean) -> Unit) {
        mrequestFailedAction = action
    }

}