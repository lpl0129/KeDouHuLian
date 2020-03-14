package com.qttx.kedouhulian.ui.common

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qttx.kedouhulian.App
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.room.DataBase
import com.qttx.kedouhulian.ui.user.LoginActivity
import com.qttx.kedouhulian.utils.*
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.*
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.common_splash_activity.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/11 0011
 */
class SplashActivity : BaseActivity() {

//    var pers = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun getLayoutId() = R.layout.common_splash_activity

    override fun processLogic(savedInstanceState: Bundle?) {
        /**
         * 设置透明状态栏
         */
        StatusBarUtils.setStatusBarAlpha(this, 0)
        StatusBarUtils.addMarginTopEqualStatusBarHeight(ll_skip)
        hasPer();
//        requsetPerMission(10001, pers)
    }

    override fun addActivityToManager() {
        super.addActivityToManager()
        if (!this.isTaskRoot) { // 当前类不是该Task的根部，那么之前启动
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) { // 当前类是从桌面启动的
                    finish() // finish掉该类，直接打开该Task中现存的Activity
                    return
                }
            }
        }
    }

    override fun liveDataListener() {
    }

    /**
     * 拥有权限
     */
//    @AfterPermissionGranted(10001)
    private fun hasPer() {

        io.reactivex.Observable.create(ObservableOnSubscribe<String> { emitter ->
            val list = DataBase.getInstance(this@SplashActivity)
                    .cityDao()
                    .getCityByPid(0)
            if (list.isEmpty()) {
                val json = Utils.getJson(com.stay.toolslibrary.utils.extension.getApplicationContext(), "city.json")
                val gson = Gson()
                val typeToken = object : TypeToken<List<RegionsBean>>() {
                }.type
                val list1 = gson.fromJson<List<RegionsBean>>(json, typeToken)
                DataBase.getInstance(com.stay.toolslibrary.utils.extension.getApplicationContext())
                        .cityDao()
                        .insertCity(list1)
            }
            emitter.onNext("1")
        }).bindScheduler()
                .bindLifecycle(this)
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                        PathUtils.initPath(PinyinUtils.ccs2Pinyin(resources.getString(com.stay.basiclib.R.string.app_name)))
                        if (!checkLogin()) {
                            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            dimissLoadingDialog()
                            JPushInterface.setAlias(this@SplashActivity, 1, getUserId())
                            showToast("登录成功")
                            val intent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                            // 融云暂未使用 先注释
                            // loginIm()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                }
                )
    }

//    override fun onPermissionsDenied(requestCode: Int, list: MutableList<String>) {
//        super.onPermissionsDenied(requestCode, list)
//        val message = PermissionsNameHelp.getPermissionsMulti(list)
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
//            //勾选了不在询问,跳转到设置界面
//            AppSettingsDialog.Builder(this).setTitle("权限申请")
//                    .setRationale(message)
//                    .setNegativeButton("暂不")
//                    .setPositiveButton("设置")
//                    .setThemeResId(R.style.AlertDialogTheme)
//                    .build()
//                    .show()
//        } else {
//            //没有勾选不在询问
//            val checkPerms = list.toList()
//
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
//            builder.setCancelable(false)
//                    .setTitle("权限申请")
//                    .setMessage(message)
//                    .setPositiveButton("设置") { dialog, which -> requsetPerMission(10001, checkPerms) }
//                    .setNegativeButton("暂不") { dialog, which -> onActivityResult(AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE, 1005, null) }
//                    .show()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
//
//
//            val checkPerms = hasPermissions(this, pers)
//            if (checkPerms == null) {
//                hasPer()
//            } else {
//                val msg = PermissionsNameHelp.getPermissionsMulti(checkPerms.toList())
//
//                val tipDialog: TipDialog = TipDialog.newInstance("$msg 将会关闭应用程序,确定不开启吗?")
//                tipDialog.setListener {
//
//                    onCancleClick {
//                        val list = Arrays.asList(*checkPerms)
//                        if (EasyPermissions.somePermissionPermanentlyDenied(this@SplashActivity, list)) {
//                            val message = PermissionsNameHelp.getPermissionsMulti(list)
//                            AppSettingsDialog.Builder(this@SplashActivity).setTitle("权限申请").setRationale(message)
//                                    .setNegativeButton("暂不")
//                                    .setPositiveButton("设置")
//                                    .build()
//                                    .show()
//                        } else {
//                            requsetPerMission(10001, checkPerms.toList())
//                        }
//                    }
//                    onSureClick {
//                        finish()
//                        android.os.Process.killProcess(android.os.Process.myPid())
//                        System.exit(0)
//                    }
//                }
//                tipDialog.show(supportFragmentManager)
//            }
//        }
    }

    private fun loginIm() {
        showLoadingDilog("正在登录")
        val rongToken = getRongToken()
        if (applicationInfo.packageName.equals(App.getProcessName(applicationContext))) {
            RongIM.connect(rongToken, object : RongIMClient.ConnectCallback() {
                override fun onTokenIncorrect() {
                    runOnUiThread {
                        showToast("重连失败,请重新登陆")
                        dimissLoadingDialog()
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                /**
                 * 连接融云成功
                 *
                 * @param userid 当前 token 对应的用户 id
                 */
                override fun onSuccess(userid: String) {
                    dimissLoadingDialog()
                    JPushInterface.setAlias(this@SplashActivity, 1, getUserId())
                    showToast("登录成功")
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                /**
                 * 连接融云失败
                 *
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                override fun onError(errorCode: RongIMClient.ErrorCode) {
                    dimissLoadingDialog()
                    showToast("重连失败,请重新登陆")
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }

    }
}