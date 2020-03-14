package com.qttx.kedouhulian.ui.user

import android.content.Intent
import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.chat_activity_set_mark.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/14 0014
 */
class EditNickNameActivity : BaseActivity() {
    private val viewModel: ChatViewModel by viewModel()
    private var mark = ""
    override fun getLayoutId(): Int {
        return R.layout.user_activity_set_nickname
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        if (intent.hasExtra("name")) {
            mark = intent.getStringExtra("name")
            noteEt.setText(mark)
            noteEt.setSelection(noteEt.text.length)
        }

        cancleTv.setOnClickListener {
            finish()
        }
        topRight.setOnClickListener {

            val text = noteEt.text.toString()
            if (text.isEmpty()) {
                showToast("请输入昵称")
            } else {
                val intent = Intent(this, EditNickNameActivity::class.java)
                intent.putExtra("name", text)
                setResult(400, intent)
                finish()
            }
        }
    }


    override fun liveDataListener() {
        viewModel.setGroupNameLiveData.toObservable(this)
        {
            showToast("设置成功")
            setResult(400)
            finish()
        }
    }

}