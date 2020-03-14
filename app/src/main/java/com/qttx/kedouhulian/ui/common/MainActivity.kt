package com.qttx.kedouhulian.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.githang.statusbar.StatusBarCompat
import com.qttx.kedouhulian.App
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.viewModel.PayViewModel
import com.qttx.kedouhulian.ui.user.LoginActivity
import com.qttx.kedouhulian.utils.*
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.library.update.ApkUpdate
import com.stay.toolslibrary.library.update.UpdateActivity
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.StatusBarUtils
import com.stay.toolslibrary.widget.DragPointView
import com.stay.toolslibrary.widget.NativeTabButton
import io.rong.imkit.RongIM
import io.rong.imkit.manager.IUnReadMessageObserver
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation

import kotlinx.android.synthetic.main.common_activity_main.*
import me.leolin.shortcutbadger.ShortcutBadger
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), IUnReadMessageObserver, DragPointView.OnDragListener {

    private lateinit var mFragments: List<Fragment>

    private lateinit var mTabButtons: List<NativeTabButton>
    private val versionViewModel: PayViewModel by viewModel()
    private val title = intArrayOf(R.string.tab_1_title, R.string.tab_2_title, R.string.tab_3_title, R.string.tab_4_title)
    private val unCheckImage = intArrayOf(R.drawable.kdsj_btn, R.drawable.gagaliao_btn, R.drawable.kedouchitang_btn, R.drawable.wode_btn)
    private val checkImage = intArrayOf(R.drawable.kdsjdj_btn, R.drawable.guagualiangdj_btn, R.drawable.kedouchitangdj_btn, R.drawable.wodedj_btn)

    private var currentIndex = -1

    override fun getLayoutId(): Int {
        return R.layout.common_activity_main
    }

    override fun liveDataListener() {
        StatusBarUtils.setStatusBarAlpha(this, 0)
//        versionViewModel.versionLiveData
//                .toObservable(this)
//                {
//
//                    it.data?.let {
//                        val apkUpdate = ApkUpdate()
//                        apkUpdate.apk = it.downloadurl
//                        apkUpdate.isForceUpdate = it.enforce == 1
//                        apkUpdate.remark = it.upgradetext
//                        apkUpdate.versionNo = it.newversion
//                        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
//                        intent.putExtra("apkUpdate", apkUpdate)
//                        startActivity(intent)
//                    }
//                }
    }


    override fun processLogic(savedInstanceState: Bundle?) {
//        versionViewModel
//                .getAppVersion(this)
        setSwipeBackEnable(false)
        EventBus.getDefault().register(this)
        initImUnReadListener()
        val fragment1 = supportFragmentManager.findFragmentById(R.id.fragment_1)
        val fragment2 = supportFragmentManager.findFragmentById(R.id.fragment_2)
        val fragment3 = supportFragmentManager.findFragmentById(R.id.fragment_3)
        val fragment4 = supportFragmentManager.findFragmentById(R.id.fragment_4)
        mFragments = mutableListOf(fragment1!!, fragment2!!, fragment3!!, fragment4!!)
        mTabButtons = mutableListOf(tab_button_1, tab_button_2, tab_button_3, tab_button_4)
        mTabButtons.forEachIndexed { index, it ->
            it.setTitle(getString(title[index]))
            it.setIndex(index)
            it.setSelectColor(R.color.primaryColor)
            it.setUnselectColor(R.color.deepColor)
            it.setSelectedImage(
                    checkImage[index])
            it.setUnselectedImage(
                    unCheckImage[index])
            it.setOnTabClick {
                setFragmentShow(it)
            }
        }
        setFragmentShow(0)

    }

    override fun onResume() {
        super.onResume()
        //
        //融云暂未使用 先注释
        // loginIm()
    }

    private fun loginIm() {

        val rongToken = getRongToken()
        val state = RongIM.getInstance()
                .currentConnectionStatus
                .value
        LogUtils.e("connectstate", state)
        if (state == 2 && rongToken.isNotEmpty()) {

            if (applicationInfo.packageName.equals(App.getProcessName(applicationContext))) {
                RongIM.connect(rongToken, object : RongIMClient.ConnectCallback() {
                    override fun onTokenIncorrect() {
                        runOnUiThread {
                            showToast("重连失败,请重新登陆")
                            dimissLoadingDialog()
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    /**
                     * 连接融云成功
                     *
                     * @param userid 当前 token 对应的用户 id
                     */
                    override fun onSuccess(userid: String) {

                    }

                    /**
                     * 连接融云失败
                     *
                     * @param errorCode 错误码，可到官网 查看错误码对应的注释
                     */
                    override fun onError(errorCode: RongIMClient.ErrorCode) {
                        runOnUiThread {
                            showToast("重连失败,请重新登陆")
                            dimissLoadingDialog()
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(filter: EventFilterBean) {
        if (EventStatus.TAB_CHANGE.equals(filter.type)) {
            setFragmentShow(Integer.parseInt(filter.value.toString()))
        }
    }

    /**
     * tab切换
     *
     * @param which
     */
    private fun setFragmentShow(which: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        mFragments.forEach {
            transaction.hide(it)
        }
        transaction.show(mFragments[which])
        transaction.commitAllowingStateLoss()
        mTabButtons.forEachIndexed { currentIndex, it ->
            it.setSelectedButton(currentIndex == which)
        }
        currentIndex = which

        if (which == 0 || which == 2) {
            StatusBarCompat.setStatusBarColor(this, resources.getColor(com.stay.basiclib.R.color.translucent))
        } else if (which != 2) {
            StatusBarCompat.setStatusBarColor(this, resources.getColor(com.stay.basiclib.R.color.whiteColor))
        }
    }

    private var mExitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次退出")
            mExitTime = System.currentTimeMillis()
            return
        } else {
            super.onBackPressed()
        }
        super.onBackPressed()
    }

    private lateinit var conversationTypes: Array<Conversation.ConversationType>

    /**
     * 注册IM未读数监听
     */
    protected fun initImUnReadListener() {
        conversationTypes = arrayOf(Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE)
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, *conversationTypes)
    }

    override fun onCountChanged(p0: Int) {
        tab_button_2.setUnreadNotify(p0)
        EventUtils.postEvent(EventStatus.MSG_COUNT_CHANGE, p0)
        ShortcutBadger.applyCount(this, p0)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this)
        super.onDestroy()
    }

    override fun onDragOut() {
        tab_button_2.setUnreadNotify(0)
        //NToast.shortToast(mContext, getString(R.string.clear_success));
        RongIM.getInstance().getConversationList(object : RongIMClient.ResultCallback<List<Conversation>>() {
            override fun onSuccess(conversations: List<Conversation>?) {
                if (conversations != null && conversations.size > 0) {
                    for (c in conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.conversationType, c.targetId, null)
                    }
                }
            }

            override fun onError(e: RongIMClient.ErrorCode) {

            }
        }, *conversationTypes)

    }
}
