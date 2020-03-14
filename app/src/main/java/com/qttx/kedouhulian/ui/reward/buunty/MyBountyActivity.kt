package com.qttx.kedouhulian.ui.reward.buunty

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.redpacket.MyGrapRedPacketFragment
import com.qttx.kedouhulian.ui.reward.redpacket.MyPublishRedPacketFragment
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import kotlinx.android.synthetic.main.reward_actviity_my_task.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 * 我的任务
 */
class MyBountyActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.reward_actviity_my_task
    }

    var index=0
    private val titles = arrayOf("发布的悬赏", "抢到的悬赏")

    private var tabList = mutableListOf<TextView>()

    override fun processLogic(savedInstanceState: Bundle?) {
        index=intent.getIntExtra("index",0)
        top_left.setOnClickListener {
            finish()
        }
        publishTaskTv.text=titles[0]
        grapTaskTv.text=titles[1]

        tabList.add(publishTaskTv)
        tabList.add(grapTaskTv)
        val fragments = arrayOf(MyPublishBountyFragment(), MyGrapBountyFragment())
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
        viewPager.currentItem = index
        setSelect(index)
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