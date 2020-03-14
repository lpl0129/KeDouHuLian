package com.qttx.kedouhulian.ui.reward.redpacket

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.MyGrapTaskFragment
import com.qttx.kedouhulian.ui.reward.task.MyPublishTaskFragment
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import kotlinx.android.synthetic.main.reward_actviity_my_task.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 * 我的红包
 */
class MyRedPacketActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.reward_actviity_my_task
    }

    private val titles = arrayOf("发布的广告", "抢到的红包")

    private var tabList = mutableListOf<TextView>()

    override fun processLogic(savedInstanceState: Bundle?) {

        top_left.setOnClickListener {
            finish()
        }
        publishTaskTv.text=titles[0]
        grapTaskTv.text=titles[1]

        tabList.add(publishTaskTv)
        tabList.add(grapTaskTv)
        val fragments = arrayOf(MyPublishRedPacketParentFragment(), MyGrapRedPacketFragment())
        val fragmentAdapter = BaseFmtAdapter(supportFragmentManager, fragments.toList() as List<BaseFragment>, titles.toList())
        viewPager.offscreenPageLimit = 2
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                setSelect(p0)
            }
        }
        )
        viewPager.adapter = fragmentAdapter
        viewPager.currentItem = 0
        setSelect(0)
        tabList.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewPager.currentItem = index
            }
        }
    }

    override fun liveDataListener() {
    }

    private fun setSelect(position: Int) {
        tabList.forEachIndexed { index, textView ->
            textView.isSelected = index == position
        }
    }
}