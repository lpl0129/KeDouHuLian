package com.qttx.kedouhulian.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.amap.api.mapcore.util.it
import com.lxj.xpopup.XPopup
import com.qiniu.android.storage.UploadManager
import com.qttx.kedouhulian.App
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.ImageBean
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.ui.chat.ChatSetGroupNameActivity
import com.qttx.kedouhulian.ui.common.ChooseCity
import com.qttx.kedouhulian.ui.user.viewModel.PerfectUserDataViewModel
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.library.picture.PictureHelper
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.widget.dialog.TipDialog
import kotlinx.android.synthetic.main.user_activity_data.*
import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File
import java.util.*

/**
 * @author huangyr
 * @date 2019/5/16 0016
 */
class UserDetailActivity : BaseActivity() {

    private val viewModel: UserInfoViewModel by viewModel()

    private val agerViewModel: PerfectUserDataViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.user_activity_data
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("个人信息")
        viewModel.getUserInfo(this)
        agerViewModel.getAge(this)
    }

    override fun liveDataListener() {
        viewModel.userinfo2LiveData
                .toObservable(this)
                {
                    it.data?.let {
                        avatarIv.loadCircleAvatar(it.avatarUrl)
                        nickerNameTv.text = it.nickname
                        cityTv.text = it.area_name
                        sexTv.text = if (it.gender == 1) "男" else "女"
                        ageTv.text =it.birthday_name
                    }
                }
        viewModel.tokenLiveData.toObservable(this)
        {
            //上传
            it.data?.let {
                upLoadImage(viewModel.avatarLocal, it)
            }

        }
        viewModel.editUserinfoLiveData.toObservable(this)
        {
            setResult(400)
            showToast("保存成功")
        }
        agerViewModel.ageListData.toObservable(this)
        {

        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getUserInfo(this)
        agerViewModel.getAge(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            ageLl -> {
                agerViewModel.ageListData.value?.data?.let {
                    val listString = mutableListOf<String>()
                    for (item in it) {
                        listString.add(item.agegruop)
                    }
                    XPopup.Builder(this)
                            .asBottomList("年龄阶段", listString.toTypedArray(), null, -1)
                            { position, text ->
                                ageTv.text = text
                                agerViewModel.id = it[position].id
                                val map = mutableMapOf<String, String>()
                                map["birthday"] = it[position].id
                                viewModel.editUserinfo(this, map)
                            }.show()
                }
            }
            modifyAvatarLl -> {
                //修改头像
                val per = listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requsetPerMission(10001, per)
            }
            nickerNameLl -> {
                //修改昵称
                val intent = Intent(this, EditNickNameActivity::class.java)
                intent.putExtra("name", nickerNameTv.text.toString())
                startActivityForResult(intent, 500)
            }
            sexLl -> {
                val list = listOf("男", "女")
                XPopup.Builder(this)
                        .asBottomList("性别", list.toTypedArray(), null, -1)
                        { position, text ->
                            sexTv.text = text
                            val map = mutableMapOf<String, String>()
                            map["gender"] = (position + 1).toString()
                            viewModel.editUserinfo(this, map)
                        }.show()
            }
            cityLl -> {
                val intent = Intent()
                intent.setClass(this, ChooseCity::class.java)
                startActivityForResult(intent, 200)
            }
            erCodeLl -> {
                //
                val intent = Intent()
                intent.setClass(this, UserQrcodeActivity::class.java)
                startActivity(intent)


            }
            commitLl -> {

            }
            loginOutTv -> {
                TipDialog.newInstance("是否退出当前账号?")
                        .setListener {
                            onSureClick {
                                App.loginOut(false)
                            }
                        }
                        .show(supportFragmentManager)
            }
        }
    }

    @AfterPermissionGranted(10001)
    private fun hasPermission() {
        getPictureHelper().setHasCrop(true)
                .setHasCamera(true)
                .setHasZip(true)
                .setMaxSize(1)
                .setHasZipDialog(true)
                .takePhoto()
    }

    override fun initViewClickListeners() {
        modifyAvatarLl.setOnClickListener(this)
        nickerNameLl.setOnClickListener(this)
        sexLl.setOnClickListener(this)
        cityLl.setOnClickListener(this)
        erCodeLl.setOnClickListener(this)
        commitLl.setOnClickListener(this)
        ageLl.setOnClickListener(this)

        loginOutTv.setOnClickListener(this)
    }

    var proBean: RegionsBean? = null
    var cityBean: RegionsBean? = null
    var disBean: RegionsBean? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200 && resultCode == 400 && data != null) {
            proBean = data.getParcelableExtra("1")
            cityBean = data.getParcelableExtra("2")
            disBean = data.getParcelableExtra("3")
            cityTv.text = proBean?.name + cityBean?.name + disBean?.name
            val map = mutableMapOf<String, String>()
            map["province"] = proBean!!.id.toString()
            map["city"] = cityBean!!.id.toString()
            map["district"] = disBean!!.id.toString()
            viewModel.editUserinfo(this, map)
        } else if (requestCode == 500 && resultCode == 400 && data != null) {
            val map = mutableMapOf<String, String>()
            map["nickname"] = data.getStringExtra("name")
            nickerNameTv.text = map["nickname"]
            viewModel.editUserinfo(this, map)
        } else {
            getPictureHelper().getPhotoList(requestCode, resultCode, data, object : PictureHelper.PictureResultListener {
                override fun error() {
                }

                override fun complete(list: List<String>, idtag: String?) {
                    if (!list.isEmpty()) {
                        viewModel.avatarLocal = list[0]
                        avatarIv.loadCircleAvatar(viewModel.avatarLocal)
                        val token = viewModel.tokenLiveData.value?.data
                        if (token.isNullOrEmpty()) {
                            viewModel.getToken(this@UserDetailActivity)
                        } else {
                            upLoadImage(viewModel.avatarLocal, token)
                        }
                    }
                }
            })
        }


    }

    private var uploadManager: UploadManager? = null

    private fun upLoadImage(localUrl: String, token: String) {
        if (loadingDialog == null || loadingDialog?.dialog == null) {
            showLoadingDilog("正在上传...")
        }
        val maxfile = File(localUrl)
        if (!maxfile.exists() || !maxfile.isFile()) {
            showToast("文件不存在")
            return
        }
        // 文件后缀
        val fileSuffix = maxfile.getName().substring(maxfile.getName().lastIndexOf("."))
        val name = UUID.randomUUID().toString() + fileSuffix

        val objectKey = "uploads/img/" + DateUtils.millis2String(System.currentTimeMillis(), DateUtils.getShortFormat()) + "/" + getUserId() + "/" + name
        if (uploadManager == null) {
            uploadManager = UploadManager()
        }
        uploadManager?.put(maxfile, objectKey, token,
                { key, info, _ ->
                    if (info.isOK) {
                        val map = mutableMapOf<String, String>()
                        map["avatar"] = key
                        viewModel.editUserinfo(this, map)
                    } else {
                        showToast("上传失败")
                    }
                }, null)


    }

}