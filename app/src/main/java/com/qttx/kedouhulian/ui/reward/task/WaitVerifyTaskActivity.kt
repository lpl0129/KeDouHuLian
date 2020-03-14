package com.qttx.kedouhulian.ui.reward.task

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import kotlinx.android.synthetic.main.common_activity_viewpager.*
import java.util.ArrayList

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class WaitVerifyTaskActivity : BaseActivity() {

    private val mFmts = mutableListOf<BaseFragment>()
    private val mTitles = mutableListOf<String>()

    private var adapter: BaseFmtAdapter? = null
    override fun getLayoutId(): Int = R.layout.common_activity_viewpager
    private var taskid: String = ""

    override fun processLogic(savedInstanceState: Bundle?) {
        taskid=intent.getStringExtra("taskid")
        setTopTitle("待审任务")
        mTitles.add("待审核")
        mTitles.add("已通过")
        mTitles.add("不合格")

        mFmts.add(WaitVerifyTaskFragment.getIntace(1,taskid))
        mFmts.add(WaitVerifyTaskFragment.getIntace(2,taskid))
        mFmts.add(WaitVerifyTaskFragment.getIntace(-1,taskid))
        adapter = BaseFmtAdapter(supportFragmentManager, mFmts, mTitles)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 3
        mTabLayout.setupWithViewPager(viewpager)
    }

    override fun liveDataListener() {
    }

}