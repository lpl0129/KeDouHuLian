package com.qttx.kedouhulian.ui.pond

import android.os.Bundle
import android.view.View
import com.amap.api.mapcore.util.it
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
class MyPondActivity : BaseActivity() {
    override fun processLogic(savedInstanceState: Bundle?) {

        setTopTitle("我的池塘")

        mTitles.add("占有池塘")
        mTitles.add("被买走池塘")
        mFmts.add(MyPondItemFragment())
        mFmts.add(MyPondSellItemFragment())
        adapter = BaseFmtAdapter(supportFragmentManager, mFmts, mTitles)
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