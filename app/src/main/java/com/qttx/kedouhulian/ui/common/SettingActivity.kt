package com.qttx.kedouhulian.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.SystemNotifyCateBean
import com.qttx.kedouhulian.ui.reward.viewModel.SettingNotifyViewModel
import com.qttx.kedouhulian.ui.user.UserDataActivity
import com.qttx.kedouhulian.utils.Utils
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.PreferenceUtil
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.common_activity_setting.*
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/5/20 0020
 */
class SettingActivity : BaseActivity() {

    companion object {
        fun show(context: Context, key: String) {
            val intent = Intent(context, SettingActivity::class.java)
            intent.putExtra("key", key)
            context.startActivity(intent)
        }
    }

    private val viewModel: SettingNotifyViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.common_activity_setting
    }

    var result = mutableListOf<SystemNotifyCateBean>()

    private lateinit var adapter: RecyclerAdapter<SystemNotifyCateBean>

    val gson = Gson()
    override fun processLogic(savedInstanceState: Bundle?) {

        viewModel.getUserInfo(this)

        val key = intent.getStringExtra("key")

        val json = Utils.getJson(this, "notify.json")

        val typeToken = object : TypeToken<List<SystemNotifyCateBean>>() {
        }.type
        val list = gson.fromJson<List<SystemNotifyCateBean>>(json, typeToken)
                .toMutableList()
        result = list.filter {
            it.`class` == key
        }.toMutableList()
        if (!result.isEmpty()) {
            setTopTitle(result[0].title)
        }
        val layoutManger=LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.layoutManager =layoutManger
        adapter = SettingAdapter(result)
        adapter.setListener {
            onItemClickListener { bean, i, view ->
                if (bean.key != "friends_notify") {
                    viewModel.setMsg(this@SettingActivity, bean.key, Math.abs(bean.value - 1).toString())
                }else if (bean.value==1)
                {
                    shieldMessage(i)
                }else
                {
                    shieldNotMessage(i)
                }

            }
        }
        recyclerView.adapter=adapter

        viewModel.getMsg(this)
    }

    override fun liveDataListener() {
        viewModel.msgLiveData
                .toObservable(this)
                {
                    it.data?.let {
                        val json = JSONObject(gson.toJson(it))

                        result.forEach {
                            if (it.key != "friends_notify") {
                                it.value = json.optInt(it.key)
                            }else
                            {
                                it.value =PreferenceUtil.getIntDefault("notify",1)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
        viewModel.setmsgLiveData.toObservable(this)
        {
            viewModel.getMsg(this, loadingStatus = ViewLoadingStatus.LOADING_NO)
        }
        viewModel.userinfo2LiveData.toObservable(this)
        {
            it.data?.let {
                avatarIv.loadCircleAvatar(it.avatar)
                usernameTv.text = it.nickname
                cityTv.text = "会员编号: ${it.id}"
            }
        }
    }
    //关闭提示音
    private fun shieldMessage(pos:Int) {
        RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, object : RongIMClient.OperationCallback() {
            override fun onSuccess() {
                PreferenceUtil.putInt("notify",0)
                result[pos].value=0
                adapter.notifyDataSetChanged()
            }

            override fun onError(errorCode: RongIMClient.ErrorCode) {

            }
        })
    }


    //开启提示音
    private fun shieldNotMessage(pos:Int) {
        RongIM.getInstance().removeNotificationQuietHours(object : RongIMClient.OperationCallback() {
            override fun onSuccess() {
                PreferenceUtil.putInt("notify",1)
                result[pos].value=1
                adapter.notifyDataSetChanged()
            }

            override fun onError(errorCode: RongIMClient.ErrorCode) {
                showToast("设置失败")
            }
        })
    }
}
