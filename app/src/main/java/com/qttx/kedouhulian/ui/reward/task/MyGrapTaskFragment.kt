package com.qttx.kedouhulian.ui.reward.task

import android.os.Bundle
import android.view.View
import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import kotlinx.android.synthetic.main.common_activity_viewpager.*
import kotlinx.android.synthetic.main.reward_fragment_home_tab.*
import java.util.ArrayList

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyGrapTaskFragment : BaseFragment() {
    override fun processLogic() {
        rootView?.findViewById<View>(R.id.top_view)?.let {
                    it.visibility=View.GONE
                }

        mTitles.add("全部")
        mTitles.add("待提交")
        mTitles.add("待审核")
        mTitles.add("待修改")

        mFmts.add(MyGrapTaskItemFragment.getIntace(4))
        mFmts.add(MyGrapTaskItemFragment.getIntace(0))
        mFmts.add(MyGrapTaskItemFragment.getIntace(1))
        mFmts.add(MyGrapTaskItemFragment.getIntace(3))

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