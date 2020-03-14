package com.qttx.kedouhulian.ui.user

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.utils.extension.loadImage
import kotlinx.android.synthetic.main.user_activty_qrcode.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/16 0016
 */
class UserQrcodeActivity : BaseActivity() {
    private val viewModel: UserInfoViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.user_activty_qrcode
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        viewModel.getQrCodeInfo(this)
        setTopTitle("我的二维码")
    }

    override fun liveDataListener() {
        viewModel.qrCodeLiveData.toObservable(this)
        {
            it.data?.let {
                nickerNameTv.text = it.nickname
                avatarIv.loadCircleAvatar(it.avatar)
                userQrCodeIv.loadImage(it.myqcode)
            }
        }

    }

}