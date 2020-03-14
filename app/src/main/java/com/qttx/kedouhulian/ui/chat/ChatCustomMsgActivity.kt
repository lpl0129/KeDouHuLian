package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.ChatCustomMsgAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatCustomMsgViewModel
import com.qttx.kedouhulian.utils.EventUtils
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.KeyboardUtils
import kotlinx.android.synthetic.main.chat_activity_custom_msg.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChatCustomMsgActivity : BaseActivity(), IListViewProvider {

    private val listModel: ChatCustomMsgViewModel by viewModel()


    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: ChatCustomMsgAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.chat_activity_custom_msg

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = ChatCustomMsgAdapter(listModel.list)
        adapter.setListener {
            onItemChildClickListener { chatApplyBean, i, view ->
                listModel.delMsg(this@ChatCustomMsgActivity, chatApplyBean.gc_id, i)
            }
            onItemClickListener { chatCustomMsgBean, i, view ->
                if (!adapter.isEdit) {
                    KeyboardUtils.hideSoftInput(this@ChatCustomMsgActivity)
                    val intent = Intent()
                    intent.putExtra("text", chatCustomMsgBean.content)
                    setResult(300,intent)
                    finish()
                }
            }
        }
    }

    override fun liveDataListener() {
        listModel.delLiveData.toObservable(this)
        {
            listModel.list.removeAt(listModel.pos)
            adapter.notifyDataSetChanged()
        }
        listModel.addLiveData.toObservable(this)
        {
            editEt.setText("")
            ptrRequestListener(true)
        }
        listModel.listManagerLiveData.toObservableList(this, this)

    }

    private var id = ""

    override fun processLogic(savedInstanceState: Bundle?) {
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id")
        }
        setTopTitle("自定义设置", "完成")
        {
            val text = editEt.text.toString()
            listModel.addMsg(this, text)
        }
        editEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    top_right.isEnabled = false
                    top_right.setTextColor(resources.getColor(R.color.tintColor))
                    editTpTv.text = "0／50个字"
                } else {
                    top_right.isEnabled = true
                    top_right.setTextColor(resources.getColor(R.color.primaryColor))
                    val text = editEt.text.toString()
                    editTpTv.text = "${text.length}／50个字"
                }
            }

        }
        )
        editTv.setOnClickListener {

            adapter.isEdit = !adapter.isEdit
            adapter.notifyDataSetChanged()

            editTv.text = if (adapter.isEdit) "保存" else "编辑"
        }
        ptrRequestListener(true)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        ptrRequestListener(true)
    }


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }
}
