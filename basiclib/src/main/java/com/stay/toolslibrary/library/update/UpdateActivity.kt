package com.stay.toolslibrary.library.update

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import com.stay.basiclib.R
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.ActivityManagerUtils
import com.stay.toolslibrary.utils.PathUtils
import com.trello.rxlifecycle2.android.ActivityEvent

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.common_activity_update.*
import pub.devrel.easypermissions.AfterPermissionGranted


/**
 * 软件更新页面
 */
class UpdateActivity : BaseActivity() {


    private var apkUpdate: ApkUpdate? = null
    private var apkFile: File? = null
    private var fileDir: String? = null
    private var fileName: String? = null
    private var isUpdating = false

    private var disposable: Disposable? = null

    override fun getLayoutId(): Int {
        return R.layout.common_activity_update
    }


    private fun initView() {
        txt_update_msg.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun initListener() {
        //点击关闭
        btn_not!!.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        //点击立即更新
        val listPer = listOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        btn_yes!!.setOnClickListener { requsetPerMission(10001, listPer) }
    }

    private fun init() {
        apkUpdate = intent.extras!!.getSerializable("apkUpdate") as ApkUpdate
        if (apkUpdate == null) {
            return
        }
        txt_version_name!!.text = "V" + apkUpdate!!.versionNo
        txt_update_msg!!.text = "更新内容：\n" + apkUpdate!!.remark
        if (!apkUpdate!!.isForceUpdate) {
            btn_not!!.visibility = View.VISIBLE
        } else {
            this@UpdateActivity.setFinishOnTouchOutside(false)
        }
        fileDir = PathUtils.PATH_FILE
        fileName = System.currentTimeMillis().toString() + ".apk"
        apkFile = File(fileDir, fileName)
    }

    /**
     * 下载apk
     */
    private fun downloadApk() {
        Observable.create(ObservableOnSubscribe<Int> { e ->
            val url1 = URL(apkUpdate!!.apk)
            val connection = url1.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("Charset", "UTF-8")

            connection.setRequestProperty("Accept-Encoding", "gzip, deflate")

            //                connection.setDoOutput(true);
            //                connection.setDoInput(true);
            connection.useCaches = false
            //打开连接
            connection.connect()
            //获取内容长度
            val contentLength = connection.contentLength
            val inputStream = connection.inputStream
            //输出流
            val fileOutputStream = FileOutputStream(apkFile)
            val bytes = ByteArray(1024)
            var totalReaded: Long = 0
            var temp_Len: Int

            do {
                temp_Len = inputStream.read(bytes)

                if (temp_Len != -1) {
                    totalReaded += temp_Len.toLong()
                    val progressSize = (totalReaded / contentLength.toDouble() * 100).toInt()
                    e.onNext(progressSize)
                    fileOutputStream.write(bytes, 0, temp_Len)
                }else{
                    break
                }
            } while (true)

            fileOutputStream.close()
            inputStream.close()
            connection.disconnect()
            e.onComplete()
        }).subscribeOn(Schedulers.io())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onNext(t: Int) {
                        txt_progress!!.text = "更新中...$t%"
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onError(e: Throwable) {
                        txt_progress!!.text = "暂时无法更新"
                    }

                    override fun onComplete() {
                        //更新包文件
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        if (Build.VERSION.SDK_INT >= 24) {
                            // Android7.0及以上版本
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            val contentUri = FileProvider.getUriForFile(this@UpdateActivity, "$packageName.FileProvider", apkFile!!)
                            //参数二:应用包名+".fileProvider"(和步骤二中的Manifest文件中的provider节点下的authorities对应)
                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                        } else {
                            // Android7.0以下版本
                            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }

                }
                )

    }

    /**
     * 下载更新
     */
    @AfterPermissionGranted(10001)
    private fun toUpdate() {
        if (isUpdating) {
            toStopDownload()
            ActivityManagerUtils.getActivityManager().finishAllActivity()
        } else {
            isUpdating = true
            btn_yes!!.text = "放弃更新"
            btn_not!!.visibility = View.GONE
            lLayout_updating!!.visibility = View.VISIBLE
            downloadApk()
        }
    }

    /**
     * 停止下载，删除已下载文件
     */
    private fun toStopDownload() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
        if (apkFile!!.exists()) {
            apkFile!!.delete()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else true
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        initView()
        initListener()
        init()
    }

    override fun liveDataListener() {

    }

    companion object {

        /**
         * 获取手机下载目录
         *
         * @return
         */
        val downloadDir: File
            get() {
                val download_dir = File(Environment.getExternalStorageDirectory().toString() + "/Download/")
                if (!download_dir.exists()) {
                    download_dir.mkdirs()
                }

                return download_dir
            }
    }
}
