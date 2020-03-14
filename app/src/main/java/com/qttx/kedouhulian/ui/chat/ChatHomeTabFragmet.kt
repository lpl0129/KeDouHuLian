package com.qttx.kedouhulian.ui.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.PayBean
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.common.SettingActivity
import com.qttx.kedouhulian.ui.dialog.ChartMenuPop
import com.qttx.kedouhulian.ui.dialog.PondDialog
import com.qttx.kedouhulian.ui.pond.MyPondActivity
import com.qttx.kedouhulian.ui.reward.task.MyTaskActivity
import com.qttx.kedouhulian.ui.user.UserDataActivity
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.library.zxing.CaptureActivity
import com.stay.toolslibrary.utils.StatusBarUtils
import com.stay.toolslibrary.utils.extension.dp2px
import io.rong.imkit.RongContext
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imkit.widget.adapter.ConversationListAdapter
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.chat_fragment_home_tab.*
import pub.devrel.easypermissions.AfterPermissionGranted
import java.lang.StringBuilder


/**
 * @author huangyr
 * @date 2019/4/10 0010
 */

/**
 * 蝌蚪赏金首页
 */
class ChatHomeTabFragmet : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.chat_fragment_home_tab

    override fun liveDataListener() {


    }

    override fun processLogic() {
        top_right_tv.setOnClickListener {

            val intent = Intent(appContext, ChatFirendActivity::class.java)
            startActivityForResult(intent, 200)
        }
        searchEt.setOnClickListener {

            val intent: Intent = Intent(appContext, SealSearchActivity::class.java)
            startActivity(intent)
        }
        setTopTitle("呱呱聊", R.drawable.liantianzhankai_btn)
        {
            val dialog = ChartMenuPop(appContext)
                    .setOffsetXAndY((130).dp2px(), (135).dp2px())
                    .setOnSelectListener(OnSelectListener { position, text ->
                        when (position) {
                            0 -> {
                                //添加好友
                                val intent = Intent(appContext, ChatSearchUserActivity::class.java)
                                startActivityForResult(intent, 200)
                            }
                            1 -> {
                                //创建私聊群
                                val intent = Intent(appContext, ChatInviteToGroupActivity::class.java)
                                intent.putExtra("owner", true)
                                startActivityForResult(intent, 200)
                            }
                            2 -> {
                                //创建公开群
                                val intent = Intent(appContext, ChatInviteToGroupActivity::class.java)
                                intent.putExtra("owner", true)
                                intent.putExtra("type", 1)
                                startActivityForResult(intent, 200)
                            }
                            3 -> {
                                val intent = Intent(appContext, ChatFirendActivity::class.java)
                                startActivityForResult(intent, 200)
                            }
                            4 -> {
                                SettingActivity.show(appContext, "chat")
                            }
                        }
                    }
                    )
            XPopup.Builder(appContext)
                    .hasShadowBg(false)
                    .atView(attach)
                    .asCustom(dialog)
                    .show()
        }
        StatusBarUtils.addMarginTopEqualStatusBarHeight(topView)
        topLeftIv.setImageResource(R.drawable.saoyisao_btn)
        topLeftIv.setOnClickListener {

            val per = listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requsetPerMission(10001, per)
        }
        childFragmentManager
                .beginTransaction()
                .replace(R.id.chat_fragment_layout, initConversationList())
                .commitAllowingStateLoss()
    }

    @AfterPermissionGranted(10001)
    private fun hasPermission() {

        val intent = Intent(appContext, CaptureActivity::class.java)
        startActivityForResult(intent, 200)
    }

    /**
     * 会话列表的fragment
     */
    private var mConversationListFragment: ConversationListFragment? = null

    private fun initConversationList(): Fragment {
        if (mConversationListFragment == null) {
            val listFragment = ConversationListFragment()
            listFragment.setAdapter(ConversationListAdapter(RongContext.getInstance()))
            val uri: Uri
            uri = Uri.parse("rong://" + appContext.packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build()
            listFragment.uri = uri
            mConversationListFragment = listFragment

        }
        return mConversationListFragment!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            data?.let {
                val result = it.getStringExtra("result")

                if (result.isNotEmpty() && result.contains("kedouhulian")) {
                    val builder = StringBuilder(result)
                    builder.delete(0, 12)
                    if (builder.toString() == getUserId()) {
                        showToast("不能添加自己!")
                    } else {
                        UserDataActivity.show(appContext, builder.toString())
                    }
                } else {
                    showToast("请扫描正确的名片")
                }

            }
        } else if (resultCode == 400) {
            data?.let {
                if (it.hasExtra("paybean")) {
                    val payBean = it.getParcelableExtra<PayBean>("paybean")
                    PayActivity.newInstance(payBean)
                            .setListener(PayResult {
                                showToast("创建成功")
                            }
                            )
                            .show(childFragmentManager)
                }
            }
        }
    }
}
