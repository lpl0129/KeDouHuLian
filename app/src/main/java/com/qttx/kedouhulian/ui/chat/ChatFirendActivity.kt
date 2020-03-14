package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.chat.adapter.ChatContactAdapter
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.ui.user.UserDataActivity
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.PinyinUtils
import kotlinx.android.synthetic.main.chat_activity_firend.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/10 0010
 */
/**
 * 我的好友
 */
class ChatFirendActivity : BaseActivity() {

    private val viewModel: ChatViewModel by viewModel()

    private lateinit var adapter: ChatContactAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    override fun getLayoutId(): Int {
        return R.layout.chat_activity_firend

    }

    override fun initBeforeListener() {
        super.initBeforeListener()
        adapter = ChatContactAdapter(viewModel.friendList)
        adapter.type = 1

        adapter.setListener {
            onItemClickListener { friendBean, i, view ->
                //用户主页
                val intent = Intent(this@ChatFirendActivity, UserDataActivity::class.java)
                intent.putExtra("id", friendBean.uid)
                startActivityForResult(intent,200)
            }
        }
        mLayoutManager = LinearLayoutManager(this)
        //垂直方向
        mLayoutManager.orientation = OrientationHelper.VERTICAL
        //给RecyclerView设置布局管理器
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter
    }


    override fun processLogic(savedInstanceState: Bundle?) {
        bar.setTextView(select)
        bar.setOnTouchBarListener {
            viewModel.friendList
                    .forEachIndexed { index, friendBean ->
                        if (friendBean.pyname == it) {
                            mLayoutManager.scrollToPositionWithOffset(index, 1)
                            return@forEachIndexed
                        }
                    }
        }
        setTopTitle("我的好友", R.drawable.tianjiahaoyou_btn)
        {
            val intent=Intent(this,ChatSearchUserActivity::class.java)
            startActivityForResult(intent,200)

        }
        viewModel.getFirendList(this)
        newFirendLl.setOnClickListener {
            val intent = Intent(this, ChatApplyActivity::class.java)
            startActivityForResult(intent, 200)
        }
        charGroupLl.setOnClickListener {
            val intent = Intent(this, ChatGroupListActivity::class.java)
            startActivityForResult(intent, 200)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.friendNumLiveData(this)
    }

    override fun liveDataListener() {
        viewModel.friendLiveData
                .toObservable(this)
                {
                    viewModel.friendList.clear()
                    it.data?.list?.let {

                        it.forEach { item ->
                            item.pyname = PinyinUtils.getSurnameFirstLetter(item.mark)
                            if (!checkLetter(item.pyname)) {
                                item.pyname = "#"
                            }
                            viewModel.friendList.add(item)
                        }
                        val list=   viewModel.friendList.sortedBy {
                            it.pyname
                        }
                        viewModel.friendList.clear()
                        viewModel.friendList.addAll(list)
                    }
                    adapter.isInitEmpty = false
                    adapter.notifyDataSetChanged()
                }
        viewModel.friendNumLiveData
                .toObservable(this)
                {
                    it.data?.let {
                        if (it.num > 0) {
                            newFirendIv.visibility = View.VISIBLE
                        } else {
                            newFirendIv.visibility = View.GONE
                        }
                    }
                }
    }


    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getFirendList(this)
    }

    private fun checkLetter(str: String): Boolean {
        val c = str[0]
        return if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
            true
        } else {
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==400)
        {
            viewModel.getFirendList(this)
        }
    }
}