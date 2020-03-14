package com.qttx.kedouhulian.ui.user

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SmsSendStatus
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.ui.user.viewModel.RegisterViewModel
import com.qttx.kedouhulian.ui.user.viewModel.SMSViewModel
import com.qttx.kedouhulian.utils.MsgType
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.user_activity_register.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/2 0002
 */
class RegisterActivity : BaseActivity() {

    var isShowPsw = false
    var isChecked = true
    private val viewModel: RegisterViewModel by viewModel()
    private val sendMsgViewModel: SMSViewModel by viewModel()


    override fun getLayoutId(): Int {
        return R.layout.user_activity_register
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        dealTv.setOnClickListener {
            MineNotifyWebActivity.show(this,"用户协议",BuildConfig.BaseUrl+"Wxview/userAgreement")
        }
    }

    /**
     * 注册liveData监听
     */
    override fun liveDataListener() {
        viewModel.userliveData.toObservable(this)
        {
            it?.data?.apply {
                if (whether_info == 0) {
                    //完善资料
                    val intent = Intent(this@RegisterActivity, PerfectUserDataActivity::class.java)
                    intent.putExtra("user", it.data)
                    startActivityForResult(intent, 200)
                } else {
                    val intent = Intent()
                    intent.putExtra("bean", this)
                    setResult(400)
                    finish()
                }

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
                sendMsgViewModel.sendMsg(
                    this, phoneEditText.text.toString(), MsgType.REGISGTER, false
                )
            }
            seePsdIv -> {
                showPswOrNot()
            }
            deal_ll -> {
                isChecked = !isChecked
                dealIv.setImageResource(if (isChecked) R.drawable.xieyiyixuanze_btn else R.drawable.xieyiweixuan_btn)
            }
            registerTv -> {
                if (!isChecked)

                {
                    showToast("请同意用户协议")
                }else
                {
                    viewModel.register(
                            this, phoneEditText.text.toString(), signCodeEdit.text.toString(),
                            psdEditText.text.toString(), inviteCodeEdit.text.toString()
                    )
                }

            }
            loginTv -> {
                finish()
            }

        }
    }

    override fun initViewClickListeners() {
        getCode.setOnClickListener(this)
        seePsdIv.setOnClickListener(this)
        deal_ll.setOnClickListener(this)
        registerTv.setOnClickListener(this)
        loginTv.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200 && resultCode == 400) {
            val intent = Intent()
            intent.putExtra("bean", viewModel.userliveData.value?.data)
            setResult(400)
            finish()
        }
    }
}