package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.amap.api.mapcore.util.it
import com.qiniu.android.storage.UploadManager
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.ImageBean
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskSubmitViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.qttx.kedouhulian.weight.UploadDaiYanImageLayout
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.library.picture.PictureHelper
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.widget.UploadImageLayout
import kotlinx.android.synthetic.main.reward_task_activity_submit.*

import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File
import java.util.*


/**
 * 提交任务
 */
class TaskSubmitActivity : BaseActivity() {

    private lateinit var order_id: String

    private var content: String? = null
    private var cnd: String? = null
    private var imgs_list: Array<String>? = null


    private val viewModel: TaskSubmitViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.reward_task_activity_submit

    private var uploadManager: UploadManager? = null

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("提交任务")
        order_id = intent.getStringExtra("id")

        if (intent.hasExtra("content")) {
            content = intent.getStringExtra("content")
            content?.let {
                des_et.setText(it)
            }

        }

        if (intent.hasExtra("image")) {
            cnd = intent.getStringExtra("cdn")
            imgs_list = intent.getStringArrayExtra("image")
            taskImageLayout.setImageList(cnd, imgs_list!!.toMutableList())
        } else {
            taskImageLayout.setImageList(listOf())
        }


        taskImageLayout.setUploadManager(object : UploadDaiYanImageLayout.onUploadManager {
            override fun onDelete(parentPos: Int, childrenPos: Int) {

            }

            override fun onSelect(parentPos: Int, limitsize: Int) {
                viewModel.limitsize = limitsize
                val per = listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requsetPerMission(10001, per)
            }
        })

        submitTv.setOnClickListener {

            if (checkData()) {
                val token = viewModel.tokenLiveData.value?.data
                if (token.isNullOrEmpty()) {
                    viewModel.getToken(this)
                } else {
                    val beginImageSize = taskImageLayout.imageList.size
                    upLoadImage(beginImageSize, token)
                }
            }
        }
    }

    private fun checkData(): Boolean {
        val content = des_et.text.toString()
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容")
            return false
        }

        val imageList = taskImageLayout.imageList
        if (imageList.isEmpty()) {
            showToast("请上传任务配图")
            return false
        }
        return true
    }

    @AfterPermissionGranted(10001)
    private fun hasPermission() {
        getPictureHelper().setHasCrop(false)
                .setHasCamera(true)
                .setHasZip(true)
                .setMaxSize(viewModel.limitsize)
                .setHasZipDialog(true)
                .takePhoto()
    }

    override fun liveDataListener() {
        viewModel.tokenLiveData.toObservable(this)
        {
            //上传
            it.data?.let {

                val beginImageSize = taskImageLayout.imageList.size
                upLoadImage(beginImageSize, it)
            }
        }
        viewModel.submitLiveData.toObservable(this)
        {
            setResult(400)
            showToast("提交成功,等待审核")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getPictureHelper().getPhotoList(requestCode, resultCode, data, object : PictureHelper.PictureResultListener {
            override fun error() {
            }

            override fun complete(list: List<String>, idtag: String?) {
                if (!list.isEmpty()) {
                    taskImageLayout.addLocalImage(list)
                }
            }
        })
    }

    private fun upLoadImage(beginsize: Int, token: String) {
        var beginsize = beginsize
        if (loadingDialog == null || loadingDialog?.dialog == null) {
            showLoadingDilog("正在上传...")
        }
        beginsize -= 1
        if (beginsize >= 0) {

            if (taskImageLayout.imageList[beginsize].isFromService) {
                upLoadImage(beginsize, token)
                return
            }

            val localUrl = taskImageLayout.imageList[beginsize].local
            val maxfile = File(localUrl)
            if (!maxfile.exists() || !maxfile.isFile()) {
                taskImageLayout.imageList.removeAt(beginsize)
                upLoadImage(beginsize, token)
                return
            }
            // 文件后缀
            val fileSuffix = maxfile.getName().substring(maxfile.getName().lastIndexOf("."))
            val name = UUID.randomUUID().toString() + fileSuffix

            val objectKey = "uploads/img/" + DateUtils.millis2String(System.currentTimeMillis(), DateUtils.getShortFormat()) + "/" + getUserId() + "/" + name

            val finalBeginsize = beginsize


            if (uploadManager == null) {
                uploadManager = UploadManager()
            }
            uploadManager?.put(maxfile, objectKey, token,
                    { key, info, _ ->
                        if (info.isOK) {
                            taskImageLayout.imageList[finalBeginsize].img = key
                        } else {
                            taskImageLayout.imageList.removeAt(finalBeginsize)
                        }
                        upLoadImage(finalBeginsize, token)
                    }, null)

        } else {
            dimissLoadingDialog()
            saveData()
        }
    }

    private fun saveData() {
        val params = mutableMapOf<String, String>()

        params["order_id"] = order_id


        params["content"] = des_et.text.toString()

        val builder = StringBuilder()
        taskImageLayout.imageList.forEach {
            builder.append(it.img)
                    .append(",")
        }
        if (builder.isEmpty()) {
            showToast("配图上传失败,请重新上传")
            return
        } else {
            params["imgs"] = builder.deleteCharAt(builder.length - 1).toString()
        }

        viewModel.saveData(this, params)
    }
}
