package com.qttx.kedouhulian.ui.trade

import android.os.Bundle
import android.view.View
import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import kotlinx.android.synthetic.main.common_activity_viewpager.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyBuyTradeFragment : BaseFragment() {
    override fun processLogic() {
        rootView?.findViewById<View>(R.id.top_view)?.let {
                    it.visibility=View.GONE
                }

        mTitles.add("待付款")
        mTitles.add("已购买")
        mFmts.add(MyBuyTradeItemFragment.getIntace(0))
        mFmts.add(MyBuyTradeItemFragment.getIntace(1))
        adapter = BaseFmtAdapter(childFragmentManager, mFmts, mTitles)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(viewpager)
    }

    private val mFmts = mutableListOf<BaseFragment>()
    private val mTitles = mutableListOf<String>()

    private var adapter: BaseFmtAdapter? = null
    override fun getLayoutId(): Int = R.layout.common_activity_viewpager


    override fun liveDataListener() {
    }

}