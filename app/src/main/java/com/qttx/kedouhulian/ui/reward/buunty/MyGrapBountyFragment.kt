package com.qttx.kedouhulian.ui.reward.buunty

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
class MyGrapBountyFragment : BaseFragment() {
    override fun processLogic() {
        rootView?.findViewById<View>(R.id.top_view)?.let {
                    it.visibility=View.GONE
                }

        mTitles.add("全部")
        mTitles.add("进行中")
        mTitles.add("已完成")
        mTitles.add("已拒绝")

        mFmts.add(MyGrapBountyItemFragment.getIntace(0))
        mFmts.add(MyGrapBountyItemFragment.getIntace(1))
        mFmts.add(MyGrapBountyItemFragment.getIntace(2))
        mFmts.add(MyGrapBountyItemFragment.getIntace(-1))

        adapter = BaseFmtAdapter(childFragmentManager, mFmts, mTitles)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 4
        mTabLayout.setupWithViewPager(viewpager)
    }

    private val mFmts = mutableListOf<BaseFragment>()
    private val mTitles = mutableListOf<String>()

    private var adapter: BaseFmtAdapter? = null
    override fun getLayoutId(): Int = R.layout.common_activity_viewpager


    override fun liveDataListener() {
    }

}