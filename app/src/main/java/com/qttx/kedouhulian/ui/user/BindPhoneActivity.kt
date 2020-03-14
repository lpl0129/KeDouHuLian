package com.qttx.kedouhulian.ui.user

import android.arch.lifecycle.Observer
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.amap.api.mapcore.util.it
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SmsSendStatus
import com.qttx.kedouhulian.ui.user.viewModel.LoginViewModel
import com.qttx.kedouhulian.ui.user.viewModel.RegisterViewModel
import com.qttx.kedouhulian.ui.user.viewModel.SMSViewModel
import com.qttx.kedouhulian.utils.MsgType
import com.stay.toolslibrary.base.BaseActivity
import io.rong.imageloader.utils.L
import kotlinx.android.synthetic.main.user_activity_bind_phone.*

import org.koin.android.viewmodel.ext.android.viewModel


class BindPhoneActivity : BaseActivity() {

    private val viewModel: RegisterViewModel by viewModel()
    private val sendMsgViewModel: SMSViewModel by viewModel()
    var code: String = ""
    var isShowPsw = false
    var platform: String = ""
    var json: String = ""
    override fun getLayoutId(): Int = R.layout.user_activity_bind_phone

    override fun processLogic(savedInstanceState: Bundle?) {
        code = intent.getStringExtra("code")
        json = intent.getStringExtra("json")
        platform = intent.getStringExtra("platform")
    }

    override fun liveDataListener() {

        viewModel.userliveData
            .toObservable(this)
            {
                it.data?.let {
                    showToast("绑定成功")
                    val intent = Intent()
                    intent.putExtra("bean", it)
                    setResult(400,intent)
                    finish()
                }
            }
        sendMsgViewModel.smsLiveData.toObservable(this)
        {

        }

        sendMsgViewModel.timeLiveData.observe(this, Observer {
            val status = it!!.status
            when (status) {
                SmsSendStatus.onSendFailed -> {
                    getCode.isSelected = false
                    getCode.isClickable = true
                    getCode.text = "重新获取"
                }
                SmsSendStatus.onSendCodeBegin -> {
                    getCode.requestFocus()
                    getCode.isClickable = false
                    getCode.isSelected = true
                    getCode.text = "(60)获取"
                }
                SmsSendStatus.onTimeComplete -> {
                    getCode.isSelected = false
                    getCode.isClickable = true
                    getCode.text = "重新获取"
                }
                SmsSendStatus.onTimeCountChange -> {
                    getCode.text = "(${it.time})获取"
                }
            }
        })
        sendMsgViewModel.checkPhoneLiveData.observe(this, Observer {
            it?.let {
                if (!it) {
                    //存在
                    psdLl.visibility = View.VISIBLE
                }
            }
        })
    }

    /**
     * 密码是否明文显示
     */
    private fun showPswOrNot() {
        isShowPsw = !isShowPsw
        if (isShowPsw) {
            psdEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            psdEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        psdEditText.setSelection(psdEditText.text.length)
        seePsdIv.setImageResource(if (isShowPsw) R.drawable.zhengyanjing_btn else R.drawable.biyanjing_btn)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            getCode -> {
                sendMsgViewModel.sendMsg(this, phoneEditText.text.toString(), MsgType.bind, null)
            }
            seePsdIv -> {
                showPswOrNot()
            }
            bindTv -> {
                if (sendMsgViewModel.checkPhoneLiveData.value != null && sendMsgViewModel.checkPhoneLiveData.value!!) {
                    //如果手机号存在
                    viewModel.bindPhone(
                        this, phoneEditText.text.toString(),
                        signCodeEdit.text.toString(), platform, code, json
                    )
                } else {
                    //不存在
                    viewModel.register(
                        this, phoneEditText.text.toString(),
                        signCodeEdit.text.toString(), psdEditText.text.toString(),
                        inviteCodeEdit.text.toString(), platform, code, json
                    )
                }
            }
            back -> {
                finish()
            }
        }
    }

    override fun initViewClickListeners() {
        getCode.setOnClickListener(this)
        seePsdIv.setOnClickListener(this)
        bindTv.setOnClickListener(this)
        back.setOnClickListener(this)
    }
}
