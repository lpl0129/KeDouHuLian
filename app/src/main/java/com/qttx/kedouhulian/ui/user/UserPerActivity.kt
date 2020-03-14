package com.qttx.kedouhulian.ui.user

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.room.DataBase
import com.qttx.kedouhulian.ui.common.MineNotifyWebActivity
import com.qttx.kedouhulian.utils.Utils
import com.qttx.kedouhulian.utils.checkLogin
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.PathUtils
import com.stay.toolslibrary.utils.PermissionsNameHelp
import com.stay.toolslibrary.utils.PinyinUtils
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register_per.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * @author huangyr
 * @date 2019-08-29
 */
class UserPerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_register_per
    }

    var pers = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
    override fun processLogic(savedInstanceState: Bundle?) {
        registerTv.setOnClickListener {
            requsetPerMission(10001, pers)
        }
        cancletv.setOnClickListener {
            finish()
        }
        tangzhudealTv.setOnClickListener {
            MineNotifyWebActivity.show(this, "隐私协议", BuildConfig.BaseUrl + "Wxview/tangzhuxieyi")
        }
        dealTv.setOnClickListener {
            MineNotifyWebActivity.show(this,"用户协议",BuildConfig.BaseUrl+"Wxview/userAgreement")
        }
        dealIv.setOnClickListener {

        }
    }

    override fun liveDataListener() {
    }

    override fun onPermissionsDenied(requestCode: Int, list: MutableList<String>) {
        super.onPermissionsDenied(requestCode, list)
        val message = PermissionsNameHelp.getPermissionsMulti(list)
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            //勾选 了不在询问,跳转到设置界面
            AppSettingsDialog.Builder(this).setTitle("权限申请")
                    .setRationale(message)
                    .setNegativeButton("暂不")
                    .setPositiveButton("设置")
                    .setThemeResId(R.style.AlertDialogTheme)
                    .build()
                    .show()
        } else {
            //没有勾选不在询问
            val checkPerms = list.toList()

            val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            builder.setCancelable(false)
                    .setTitle("权限申请")
                    .setMessage(message)
                    .setPositiveButton("设置") { dialog, which -> requsetPerMission(10001, checkPerms) }
                    .setNegativeButton("暂不") { dialog, which -> onActivityResult(AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE, 1005, null) }
                    .show()
        }
    }

    @AfterPermissionGranted(10001)
    private fun hasPer() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {


            val checkPerms = hasPermissions(this, pers)
            if (checkPerms == null) {
                hasPer()
            } else {
                val msg = PermissionsNameHelp.getPermissionsMulti(checkPerms.toList())

                val tipDialog: TipDialog = TipDialog.newInstance("$msg 将会关闭应用程序,确定不开启吗?")
                tipDialog.setListener {

                    onCancleClick {
                        val list = Arrays.asList(*checkPerms)
                        if (EasyPermissions.somePermissionPermanentlyDenied(this@UserPerActivity, list)) {
                            val message = PermissionsNameHelp.getPermissionsMulti(list)
                            AppSettingsDialog.Builder(this@UserPerActivity).setTitle("权限申请").setRationale(message)
                                    .setNegativeButton("暂不")
                                    .setPositiveButton("设置")
                                    .build()
                                    .show()
                        } else {
                            requsetPerMission(10001, checkPerms.toList())
                        }
                    }
                    onSureClick {
                        finish()
                        android.os.Process.killProcess(android.os.Process.myPid())
                        System.exit(0)
                    }
                }
                tipDialog.show(supportFragmentManager)
            }
        }
    }
}