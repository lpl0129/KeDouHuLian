package com.qttx.kedouhulian.ui.reward.task

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.amap.api.mapcore.util.li
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskComplainViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.RegexUtils
import com.stay.toolslibrary.utils.showToast
import kotlinx.android.synthetic.main.reward_activity_task_complain.*
import kotlinx.android.synthetic.main.reward_header_task_detail.*

import org.koin.android.viewmodel.ext.android.viewModel


class TaskComplainActivity : BaseActivity() {


    private lateinit var orderid: String

    private val viewModel: TaskComplainViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.reward_activity_task_complain

    override fun processLogic(savedInstanceState: Bundle?) {
        orderid = intent.getStringExtra("id")
        orderidText.text=orderid
        submitTv.setOnClickListener {
            val phone = phoneEt.text.toString()
            val complain = complainEt.text.toString()
            viewModel.getData(this, orderid, phone, complain)
        }
    }

    override fun liveDataListener() {
        setTopTitle("申诉")
        viewModel.liveData.toObservable(this)
        {
            showToast("申诉成功")
            setResult(400)
            finish()
        }

    }


}
