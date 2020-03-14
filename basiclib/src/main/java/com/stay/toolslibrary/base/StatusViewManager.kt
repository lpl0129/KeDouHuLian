package com.stay.toolslibrary.base

/**
 * Created by huang on 2017/7/21.
 */

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.stay.basiclib.R
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.ViewStatus


/**
 * 多状态view管理器
 */
class StatusViewManager constructor(private var mContext: Context,
                                    realView: View,
                                    @LayoutRes view_loading_id: Int,
                                    @LayoutRes view_error_id: Int,
                                    @LayoutRes view_empty_id: Int,
                                    magtopSize: Int) {
    companion object {
        /**
        
         * loading 加载id
         */
        val LAYOUT_LOADING_ID = 0

        /**
         * 空数据id
         */
        val LAYOUT_EMPTY_DATA_ID = 1
        /**
         * 异常id
         */
        val LAYOUT_ERROR_ID = 2
    }

    /**
     * 加载view
     */
    private var mLoadView: ViewStub? = null
    /**
     * 空数据界面
     */
    private var mEmptyView: ViewStub? = null
    /**
     * 无网络链接时得view
     */
    private var mErrorView: ViewStub? = null

    /**
     * 实际view的父view
     */
    private val mContentView: ViewGroup = realView.parent as ViewGroup

    private var mParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)

    /**
     * 保存状态view的container
     */
    private var mStatusContainer: FrameLayout? = null


    private var viewHolder: SparseArray<View> = SparseArray()


    private var error_iv: ImageView? = null
    private var error_tv: TextView? = null
    private var error_bt: TextView? = null


    private var empty_iv: ImageView? = null
    private var empty_tv: TextView? = null
    /**
     * 重新加载接口
     */
    private var mOnRetryClick: onRetryClick? = null


    /**
     * 设置的加载和空数据界面距离头部的距离
     */
    private val magTop = magtopSize


    init {

        mLoadView = ViewStub(mContext)
        mLoadView?.layoutResource = view_loading_id
        mEmptyView = ViewStub(mContext)
        mEmptyView?.layoutResource = view_empty_id
        mErrorView = ViewStub(mContext)
        mErrorView?.layoutResource = view_error_id
        mParams.setMargins(0, magTop, 0, 0)

    }

    /**
     * 当为Loading,empty,error时,加载并显示对应的view.隐藏其他的view
     */
    fun showStatusView(msgBean: NetMsgBean) {
        inflateLayout(msgBean)
        var keyid = getIdKeyByStatus(msgBean.status)

        if (keyid == StatusViewManager.LAYOUT_EMPTY_DATA_ID) {
            setEmpty(msgBean)
        } else if (keyid == StatusViewManager.LAYOUT_ERROR_ID) {
            setErrorData(msgBean)
        }

        for (i in 0 until viewHolder.size()) {
            val key = viewHolder.keyAt(i)
            val valueView = viewHolder.valueAt(i)

            if (key == keyid) {
                valueView.visibility = View.VISIBLE
            } else {
                valueView.visibility = (View.GONE)
            }
        }
    }

    private fun inflateLayout(errorMsgBean: NetMsgBean) {
        if (mStatusContainer == null) {
            mStatusContainer = FrameLayout(mContext)
            mStatusContainer!!.layoutParams = mParams;
            mContentView.addView(mStatusContainer)
        }

        val idkey = getIdKeyByStatus(errorMsgBean.status)
        var view: View? = null

        if (viewHolder.get(idkey) == null) {
            when (idkey) {

                StatusViewManager.LAYOUT_LOADING_ID ->
                {
                    mStatusContainer!!.addView(mLoadView)
                    view = mLoadView?.inflate()
                }

                StatusViewManager.LAYOUT_EMPTY_DATA_ID -> {
                    mStatusContainer!!.addView(mEmptyView)
                    view = mEmptyView?.inflate()
                    empty_iv = view?.findViewById(R.id.empty_iv)
                    empty_tv = view?.findViewById(R.id.empty_tv)
                }
                StatusViewManager.LAYOUT_ERROR_ID -> {
                    mStatusContainer!!.addView(mErrorView)
                    view = mErrorView?.inflate()
                    error_iv = view?.findViewById(R.id.error_iv)
                    error_tv = view?.findViewById(R.id.error_tv)
                    error_bt = view?.findViewById(R.id.error_bt)
                    error_bt?.let {
                        mOnRetryClick?.onRetryLoad(errorMsgBean)
                    }
                }
            }
            view?.let {
                viewHolder.put(idkey, view)
            }
        }
    }

    private fun setEmpty(errorMsgBean: NetMsgBean) {
        empty_tv?.text = errorMsgBean.errorMsg
        empty_iv?.let {
            if (errorMsgBean.errorImageRes == 0) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.setImageResource(errorMsgBean.errorImageRes)
            }
        }
    }

    private fun setErrorData(errorMsgBean: NetMsgBean) {
        error_tv?.text = errorMsgBean.errorMsg
        error_iv?.let {
            if (errorMsgBean.errorImageRes == 0) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.setImageResource(errorMsgBean.errorImageRes)
            }
        }
        error_bt?.let {
            if (errorMsgBean.isCanRetry) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
            it.setOnClickListener {
                mOnRetryClick?.onRetryLoad(errorMsgBean)
            }
        }

    }

    private fun getIdKeyByStatus(status: ViewStatus): Int {
        return when (status) {
            ViewStatus.LOADING_VIEW -> StatusViewManager.LAYOUT_LOADING_ID
            ViewStatus.EMPTY -> StatusViewManager.LAYOUT_EMPTY_DATA_ID
            ViewStatus.ERROR_VIEW -> StatusViewManager.LAYOUT_ERROR_ID
            else -> 3
        }
    }

    fun onDestory() {
        mEmptyView = null
        mLoadView = null
        mErrorView = null
        mStatusContainer?.removeAllViews()
        viewHolder.clear()
        mOnRetryClick = null
        mStatusContainer = null
    }

    interface onRetryClick {
        fun onRetryLoad(errorMsgBean: NetMsgBean)
    }

    fun setOnRetryClick(onRetryClick: onRetryClick) {
        mOnRetryClick = onRetryClick
    }
}

