package com.qttx.kedouhulian.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.amap.api.mapcore.util.it
import com.lxj.xpopup.XPopup
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.bean.Userinfo
import com.qttx.kedouhulian.ui.common.ChooseCity
import com.qttx.kedouhulian.ui.user.viewModel.PerfectUserDataViewModel
import com.qttx.kedouhulian.utils.setToken
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.widget.dialog.ListDialog
import kotlinx.android.synthetic.main.reward_activity_publish_task.*

import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.user_activity_perfect_user_data.*


/**
 * 完善信息
 */
class PerfectUserDataActivity : BaseActivity() {

    private val viewModel: PerfectUserDataViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.user_activity_perfect_user_data
    private lateinit var user: Userinfo
    var proBean: RegionsBean? = null
    var cityBean: RegionsBean? = null
    var disBean: RegionsBean? = null

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("完善信息")
        user = intent.getParcelableExtra("user")
        setToken(user.token)
        viewModel.getAge(this)
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getAge(this)
    }

    override fun liveDataListener() {
        viewModel.ageListData.toObservable(this)
        {

        }
        viewModel.saveAgeData.toObservable(this)
        {
            user.whether_info = 1
            val intent = Intent()
            intent.putExtra("bean", user)
            showToast("完善成功")
            setResult(400,intent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            ageLl -> {
                viewModel.ageListData.value?.data?.let {
                    val listString = mutableListOf<String>()

                    for (item in it) {
                        listString.add(item.agegruop)
                    }
                    XPopup.Builder(this)
                            .asBottomList("年龄阶段", listString.toTypedArray(), null, -1)
                            { position, text ->
                                ageTv.text = text
                                viewModel.id = it[position].id
                            }.show()
                }

            }
            sexLl -> {
                val list = listOf("男", "女")
                XPopup.Builder(this)
                        .asBottomList("性别", list.toTypedArray(), null, -1)
                        { position, text ->
                            sexTv.text = text
                            viewModel.sex = (position + 1)
                        }.show()
            }
            addressLl -> {
                val intent = Intent()
                intent.setClass(this, ChooseCity::class.java)
                startActivityForResult(intent, 200)
            }
            submitTv -> {
                viewModel.setAet(this)
            }
        }
    }

    override fun initViewClickListeners() {
        ageLl.setOnClickListener(this)
        sexLl.setOnClickListener(this)
        addressLl.setOnClickListener(this)
        submitTv.setOnClickListener (this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 400 && data != null) {
            proBean = data.getParcelableExtra("1")
            cityBean = data.getParcelableExtra("2")
            disBean = data.getParcelableExtra("3")
            addressTv.text = proBean?.name + cityBean?.name + disBean?.name

            viewModel.province = proBean?.id
            viewModel.city = cityBean?.id
            viewModel.district = disBean?.id
        }
    }
}
