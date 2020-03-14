package com.qttx.kedouhulian.ui.reward

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.BaseActivity
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.qttx.kedouhulian.ui.reward.buunty.BountyGrapFragment
import com.qttx.kedouhulian.ui.reward.redpacket.RedPacketGrapFragment
import com.qttx.kedouhulian.ui.reward.task.TaskGrapFragment
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.BaseFmtAdapter
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.ContainerActivity
import kotlinx.android.synthetic.main.reward_actviity_task_main.*

/**
 * @author huangyr
 * @date 2019/4/11 0011
 */
class RewardHunterMainActivity : BaseActivity() {

    private val titles = arrayOf("赏金任务", "广告红包", "赏金猎人")

    private var tabList = mutableListOf<TextView>()

    private var index = 0


    override fun getLayoutId(): Int = R.layout.reward_actviity_task_main

    override fun processLogic(savedInstanceState: Bundle?) {
        index=intent.getIntExtra("index",0)
        setTopTitle("任务大厅", R.drawable.souzuo_btn)
        {
            val bound = Bundle()
            bound.putInt("cate", -1)
            when (index) {
                0 ->
                    ContainerActivity.startContainerActivity(this, TaskGrapFragment::class.java.canonicalName, bound)
                1 ->
                    ContainerActivity.startContainerActivity(this, RedPacketGrapFragment::class.java.canonicalName, bound)
                2 ->
                    ContainerActivity.startContainerActivity(this, BountyGrapFragment::class.java.canonicalName, bound)
            }

        }
//          = intent.getIntExtra("index", 0)
        tabList.add(grapTaskTv)
        tabList.add(redPacketRewardTv)
        tabList.add(rewardHuntTv)
        val fragments = arrayOf(TaskGrapFragment(), RedPacketGrapFragment(), BountyGrapFragment())
        val fragmentAdapter = BaseFmtAdapter(supportFragmentManager, fragments.toList() as List<BaseFragment>, titles.toList())
        viewPager.offscreenPageLimit = 3
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
        this.index = position
        tabList.forEachIndexed { index, textView ->
            textView.isSelected = index == position
        }
    }
}