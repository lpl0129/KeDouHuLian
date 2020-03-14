package com.qttx.kedouhulian.ui.user

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.user_activity_complain.*

import org.koin.android.viewmodel.ext.android.viewModel


class UserComplainActivity : BaseActivity() {


    private val viewModel: UserInfoViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.user_activity_complain

    override fun processLogic(savedInstanceState: Bundle?) {
        submitTv.setOnClickListener {
            val phone = phoneEt.text.toString()
            val complain = complainEt.text.toString()
            viewModel.compalin(this, phone, complain)
        }
    }

    override fun liveDataListener() {
        setTopTitle("投诉建议")
        viewModel.complainliveData.toObservable(this)
        {
            showToast("提交成功")
            setResult(400)
            finish()
        }

    }


}
