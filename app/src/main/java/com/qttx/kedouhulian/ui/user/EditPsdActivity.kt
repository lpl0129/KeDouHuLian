package com.qttx.kedouhulian.ui.user

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SmsSendStatus
import com.qttx.kedouhulian.ui.user.viewModel.FindPsddViewModel
import com.qttx.kedouhulian.ui.user.viewModel.SMSViewModel
import com.qttx.kedouhulian.utils.MsgType
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.user_find_psd_activity.*

import org.koin.android.viewmodel.ext.android.viewModel


class EditPsdActivity : BaseActivity() {

    private val viewModel: FindPsddViewModel by viewModel()

    private val sendMsgViewModel: SMSViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.user_find_psd_activity
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("修改密码")
    }

    override fun liveDataListener() {
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

        viewModel.reSetliveData.toObservable(this)
        {
            showToast("找回成功")
            finish()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            getCode -> {
                sendMsgViewModel.sendMsg(
                    this, phoneEditText.text.toString(), MsgType.RESETPWD, true
                )
            }
            seePsdIv -> {
                showPswOrNot()
            }
            registerTv -> {
                viewModel.findpasd(
                    this, phoneEditText.text.toString(), signCodeEdit.text.toString(),
                    psdEditText.text.toString()
                )
            }
        }
    }

    var isShowPsw = false
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

    override fun initViewClickListeners() {
        getCode.setOnClickListener(this)
        seePsdIv.setOnClickListener(this)
        registerTv.setOnClickListener(this)
    }
}
