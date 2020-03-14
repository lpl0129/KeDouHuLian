package com.qttx.kedouhulian.ui.chat

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.stay.toolslibrary.base.BaseActivity
import io.rong.imkit.RongIM
import kotlinx.android.synthetic.main.chat_activity_set_mark.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/14 0014
 */
class ChatSetMarkActivity : BaseActivity() {
    private val viewModel: ChatViewModel by viewModel()
    private var mark = ""
    private var id = ""
    override fun getLayoutId(): Int {
        return R.layout.chat_activity_set_mark
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        if (intent.hasExtra("name")) {
            mark = intent.getStringExtra("name")
            noteEt.setText(mark)
            noteEt.setSelection(mark.length)
        }
        id = intent.getStringExtra("id")

        cancleTv.setOnClickListener {
            finish()
        }
        topRight.setOnClickListener {

            val text = noteEt.text
            if (text.isEmpty()) {
                showToast("请输入备注名")
            } else {
                viewModel.setUserMark(this, id, text.toString())
            }
        }
    }


    override fun liveDataListener() {
        viewModel.setMarkLiveData.toObservable(this)
        {
            showToast("设置成功")

            setResult(400)
            finish()
        }
    }

}