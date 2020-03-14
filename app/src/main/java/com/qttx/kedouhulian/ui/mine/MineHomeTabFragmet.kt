package com.qttx.kedouhulian.ui.mine

import android.content.Intent
import android.net.Uri
import android.view.View
import com.googlecode.mp4parser.authoring.Edit
import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.ui.common.viewModel.PayViewModel
import com.qttx.kedouhulian.ui.user.EditPsdActivity
import com.qttx.kedouhulian.ui.user.UserComplainActivity
import com.qttx.kedouhulian.ui.user.UserDetailActivity
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.qttx.kedouhulian.utils.Utils
import com.qttx.kedouhulian.utils.getUserId
import com.qttx.kedouhulian.utils.jumpToChat
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.library.update.ApkUpdate
import com.stay.toolslibrary.library.update.UpdateActivity
import com.stay.toolslibrary.utils.PhoneUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import io.rong.imkit.RongIM
import io.rong.imlib.model.UserInfo
import kotlinx.android.synthetic.main.mine_fragment_home_tab.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * @author huangyr
 * @date 2019/4/10 0010
 */

/**
 * 蝌蚪赏金首页
 */
class MineHomeTabFragmet : BaseFragment() {

    private val viewModel: UserInfoViewModel by viewModel()

    private val versionViewModel: PayViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.mine_fragment_home_tab


    override fun processLogic() {
        versionTv.text = "v ${PhoneUtils.getAppVersionName()}"
        viewModel.getUserInfoById(this, getUserId())
    }

    override fun liveDataListener() {
        viewModel.serverliveData
                .toObservable(this)
                {
                    it.data?.let {
                        jumpToChat(appContext,it,"客服")
                    }
                }
        viewModel.userinfoLiveData
                .toObservable(this)
                {
                    it.data?.let {
                        userAvatarIv.loadCircleAvatar(it.avatarUrl)
                        noteTv.text = "会员编号：KD${getUserId()}"
                        userNameTv.text = it.nickname
                        RongIM.getInstance().refreshUserInfoCache(UserInfo(it.id, it.nickname, Uri.parse(it.avatar)))
                    }
                }
        versionViewModel.versionLiveData
                .toObservable(this)
                {

                    it.data?.let {
                        val apkUpdate = ApkUpdate()
                        apkUpdate.apk = it.downloadurl
                        apkUpdate.isForceUpdate = it.enforce == 1
                        apkUpdate.remark = it.upgradetext
                        apkUpdate.versionNo = it.newversion
                        val intent = Intent(appContext, UpdateActivity::class.java)
                        intent.putExtra("apkUpdate", apkUpdate)
                        startActivity(intent)
                    }
                }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            userLl -> {
                val detail = Intent(appContext, UserDetailActivity::class.java)
                startActivityForResult(detail, 200)
            }
            serverLl -> {
                viewModel.getServerdata(this)
//                MineNotifyWebActivity.show(appContext, "联系我们", BuildConfig.BaseUrl + "Wxview/linkwe")
            }
            aboutLl -> {
                MineNotifyWebActivity.show(appContext, "关于我们", BuildConfig.BaseUrl + "Wxview/aboutUs")
            }
            updaetLl -> {
                versionViewModel
                        .getAppVersion(this)
            }
            suggestLl -> {
                val detail = Intent(appContext, UserComplainActivity::class.java)
                startActivityForResult(detail, 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 400) {
            viewModel.getUserInfoById(this, getUserId())
        }
    }

    override fun initViewClickListeners() {
        editpsdLl.setOnClickListener {
            val detail = Intent(appContext, EditPsdActivity::class.java)
            startActivityForResult(detail, 200)
        }
        userLl.setOnClickListener(this)
        contactLl.setOnClickListener(this)
        aboutLl.setOnClickListener(this)
        updaetLl.setOnClickListener(this)
        suggestLl.setOnClickListener(this)
        serverLl.setOnClickListener(this)
    }
}
