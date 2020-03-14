package com.qttx.kedouhulian.receiver

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message

import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import com.qttx.kedouhulian.bean.OssBean
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.FileUtils
import com.stay.toolslibrary.utils.ImageUtils
import com.stay.toolslibrary.utils.ToastUtils
import java.io.File
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by huangyuru on 2017/4/7.
 */

class UpLoadToOssServices : Service() {
    private var uploadManager: UploadManager? = null
    private var token: String? = null
    private var ossList = ArrayList<OssBean>()
    private val disposable: Disposable? = null
    var isStopUpload: Boolean = false

    private val hanlder = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    val position = Integer.parseInt(msg.obj.toString())
                    ossList[position].serverUrl = ossList[position].objectkey
                    ossUpload(position + 1)
                }
                2 -> {
                    if (listener != null) {
                        listener!!.onUploadFailed()
                    }
                    ToastUtils.showShort("上传失败")
                    stopSelf()
                }
                3//上传完成
                -> {
                    if (listener != null) {
                        listener!!.onUploadSuccess(ossList)
                    }
                    stopSelf()
                }
            }
        }
    }

    var listener: UploadListener? = null

    // 运行sample前需要配置以下字段为有效的值
    override fun onBind(intent: Intent): IBinder? {
        return MyBinder()
    }

    inner class MyBinder : Binder() {
        /**
         * 获取Service的方法
         *
         * @return 返回PlayerService
         */
        val service: UpLoadToOssServices
            get() = this@UpLoadToOssServices

        fun setListener(listener: UploadListener) {
            this@UpLoadToOssServices.listener = listener
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        uploadManager = UploadManager()
        this.ossList = intent.getParcelableArrayListExtra("list")
        this.token=intent.getStringExtra("token")
        ossUpload(0)
        return Service.START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        listener = null
        uploadManager = null
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    private fun ossUpload(position: Int) {
        if (position < ossList.size) {
            val localUrl = ossList[position].localUrl
            val maxfile = File(localUrl)
            if (!maxfile.exists() && maxfile.isFile) {
                ossList.removeAt(position)
                ossUpload(position)
                return
            }
            /**
             * 压缩并上传
             */
            io.reactivex.Observable.just(position)
                    .subscribeOn(Schedulers.io())
                    .map {
                        val ossBean = ossList[it]
                        if (ossBean.type == OssBean.TYPE.IMAGE) {
                            val exName = FileUtils.getFileExtension(ossBean.localUrl)
                            if ("gif".equals(exName!!, ignoreCase = true)) {
                                File(ossBean.localUrl)
                            } else File(ImageUtils.getSmallImagePath(ossBean.localUrl))
                        } else {
                            File(ossBean.localUrl)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ file ->
                        // 文件后缀
                        val fileSuffix = maxfile.getName().substring(maxfile.getName().lastIndexOf("."))
                        val name = UUID.randomUUID().toString() + fileSuffix
                        val objectKey = "uploads/img/" + DateUtils.millis2String(System.currentTimeMillis(), DateUtils.getShortFormat()) + "/" + getUserId() + "/" + name
                        // 文件标识符objectKey
                        val uploadOptions = UploadOptions(null, null, false, null, UpCancellationSignal { isStopUpload })
                        uploadManager!!.put(file, objectKey, token,
                                { key, info, res ->
                                    if (info.isOK) {
                                        hanlder.obtainMessage(1, position).sendToTarget()
                                        ossList[position].objectkey = key
                                    } else {
                                        hanlder.obtainMessage(2, position).sendToTarget()
                                    }
                                }, uploadOptions)
                    }, { hanlder.sendEmptyMessage(2) })

        } else {
            hanlder.sendEmptyMessage(3)
        }
    }

    interface UploadListener {
        fun onUploadSuccess(ossList: ArrayList<OssBean>)
        fun onUploadFailed()
    }


}
