package com.stay.toolslibrary.library.picture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.FragmentManager
import android.support.v4.content.FileProvider

import com.stay.basiclib.R
import com.stay.toolslibrary.utils.*
import com.stay.toolslibrary.widget.dialog.LoadingDialog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.SelectionCreator
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.filter.GifSizeFilter
import com.zhihu.matisse.internal.entity.CaptureStrategy

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by huang on 2017/11/6.
 * 拍照帮助类
 */

class PictureHelper : Parcelable {
    private var mCurrentPhotoPath: String? = null

    private var mCurrentPhotoUri: Uri? = null

    var activity: RxAppCompatActivity? = null

    var fragment: RxFragment? = null

    /**
     * 最大选择数
     */
    private var maxSize = 1

    /**
     * 是否在图片列表启用相机
     */
    private var hasCamera = true

    /**
     * 是否采用裁剪，单个图片有效
     */
    private var hasCrop = false

    /**
     * 是否采用压缩
     */
    private var hasZip = false

    private var hasZipDialog = true

    private var cropWidth = 1

    private var cropHight = 1

    /**
     * s
     */
    private var identification: String? = null

    protected var dialog: LoadingDialog? = null

    private var selectionCreator: SelectionCreator? = null

    /**
     * 数据结果回调
     */
    private var listener: PictureResultListener? = null

    /**
     * 默认的defug裁剪设置
     *
     * @return
     */
    private val defaultCropOption: UCrop.Options
        get() {
            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            options.setCompressionQuality(90)
            options.withAspectRatio(cropWidth.toFloat(), cropHight.toFloat())
            if (cropWidth == 1 && cropHight == 1) {
                options.setFreeStyleCropEnabled(true)
            } else {
                options.setFreeStyleCropEnabled(false)
            }
            options.setImageNeddZip(hasZip)
            return options
        }

    private val fragmentManager: FragmentManager
        get() = if (fragment != null) {
            fragment!!.childFragmentManager
        } else {
            activity!!.supportFragmentManager
        }

    fun getIdentification(): String? {
        return identification
    }

    fun setIdentification(identification: String): PictureHelper {
        this.identification = identification
        return this
    }

    fun setMaxSize(maxSize: Int): PictureHelper {
        var maxSize = maxSize
        if (maxSize <= 0) {
            maxSize = 1
        }
        this.maxSize = maxSize
        return this
    }

    fun setHasCamera(hasCamera: Boolean): PictureHelper {
        this.hasCamera = hasCamera
        return this
    }

    fun setHasCrop(hasCrop: Boolean): PictureHelper {
        this.hasCrop = hasCrop
        return this
    }

    fun setHasZip(hasZip: Boolean): PictureHelper {
        this.hasZip = hasZip
        return this
    }

    fun setHasZipDialog(hasZipDialog: Boolean): PictureHelper {
        this.hasZipDialog = hasZipDialog
        return this
    }

    fun setCropSize(width: Int, heigth: Int): PictureHelper {

        this.cropWidth = width
        this.cropHight = heigth
        return this
    }

    /**
     * 请求参数会重置为SelectionCreator
     *
     * @param selectionCreator
     * @return
     */
    fun setSelectionCreator(selectionCreator: SelectionCreator): PictureHelper {
        this.selectionCreator = selectionCreator
        return this
    }

    constructor(activity: RxAppCompatActivity) {
        this.activity = activity
    }

    constructor(fragment: RxFragment) {
        this.fragment = fragment
    }

    private constructor () {

    }

    /**
     * 用自带相机拍照
     */
    fun takePhotoWithCamera() {
        startActivity(getTakePhotoIntent(getContext()), REQUEST_TAKE_PHOTO)
    }

    /**
     * 获取拍照的Intent
     *
     * @param context
     * @return
     */
    private fun getTakePhotoIntent(context: Context): Intent {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (captureIntent.resolveActivity(context.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.absolutePath
                mCurrentPhotoUri = FileProvider.getUriForFile(context, context.packageName + ".FileProvider", photoFile)
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    val resInfoList = context.packageManager
                            .queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        context.grantUriPermission(packageName, mCurrentPhotoUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
            }
        }
        return captureIntent
    }

    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 packname 文件夹中 照片的命名规则为：bodtec_20130125_173729.jpg
     */
    @Throws(IOException::class)
    fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timeStamp = format.format(Date())
        val imageFileName = PathUtils.packname + "_" + timeStamp + ".jpg"
        // 获取保存图片的目录
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PathUtils.packname)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, imageFileName)
    }

    /**
     * 跳转到照片选择界面
     */
    fun takePhoto() {
        PathUtils.initPath(PinyinUtils.ccs2Pinyin("蝌蚪互联"))
        val defaultselectionCreator: SelectionCreator
        if (selectionCreator == null) {
            val matisse: Matisse
            if (fragment != null) {
                matisse = Matisse.from(fragment)
            } else {
                matisse = Matisse.from(activity)
            }
            defaultselectionCreator = matisse.choose(MimeType.ofImage(), true)
                    .countable(true)
                    .capture(hasCamera)
                    .captureStrategy(
                            CaptureStrategy(true, getContext().packageName + ".FileProvider"))
                    .maxSelectable(maxSize)
                    .showSingleMediaType(true)
                    .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getContext().resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
        } else {
            defaultselectionCreator = selectionCreator!!
        }
        defaultselectionCreator.forResult(REQUEST_CHOSE_PICKTURE)
    }

    fun getPhotoList(requestCode: Int, resultCode: Int, data: Intent?, listener: PictureResultListener?) {

        if (resultCode != Activity.RESULT_OK || listener == null||data==null) {
            return
        }
        if (requestCode == REQUEST_TAKE_PHOTO_CROP) {
            /**
             * 如果是裁剪后的图片直接回调
             */
            val resultUri = UCrop.getOutput(data)
            if (resultUri != null) {
                val path = resultUri.path
                val list = ArrayList<String>()
                list.add(path)
                listener.complete(list, identification)
            } else {
                listener.error()
            }

        } else if (requestCode == REQUEST_CHOSE_PICKTURE) {
            /**
             * 如果是图库返回的根据配置处理图片
             */
            val pathlist = Matisse.obtainPathResult(data)
            if (EmptyUtils.isNotEmpty(pathlist)) {
                dealPathData(pathlist, listener)
            } else {
                listener.error()
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            /**
             * 如果是相机的返回的根据配置处理图片
             */
            val imageFile = File(mCurrentPhotoPath)
            if (imageFile.exists()) {
                // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
                // ImageUtils.galleryAddPic(act, imageFilePath);
                val pathlist = ArrayList<String>()
                pathlist.add(mCurrentPhotoPath!!)
                //处理逻辑
                dealPathData(pathlist, listener)
            } else {
                listener.error()
            }
        }

    }

    private fun dealPathData(listPaths: List<String>, listener: PictureResultListener) {
        /**
         * 如果图片是单张，并且设置了要进行裁剪。
         * 跳转到裁剪界面，压缩在裁剪界面完成
         * （防止图片过大，压缩时间长时，进入裁剪界面有等待时间，体验不好）
         */

        val exName = FileUtils.getFileExtension(listPaths[0])

        if (hasCrop && listPaths.size == 1 && !"gif".equals(exName!!, ignoreCase = true)) {
            startToCrop(listPaths[0], defaultCropOption)
        } else if (hasZip) {
            if (fragment != null) {
                beginZip(listPaths, fragment!!.bindToLifecycle(), listener)
            } else {
                beginZip(listPaths, activity!!.bindToLifecycle(), listener)
            }
        } else {
            listener.complete(listPaths, identification)
        }
    }

    /**
     * Rxjava压缩图片
     *
     * @param list
     * @param lifecycleTransformer
     * @param listener
     */
    private fun beginZip(list: List<String>, lifecycleTransformer: LifecycleTransformer<String>,
                         listener: PictureResultListener) {
        Observable.create(ObservableOnSubscribe<String> { e ->
            for (path in list) {
                val exName = FileUtils.getFileExtension(path)
                if ("gif".equals(exName!!, ignoreCase = true)) {
                    e.onNext(path)
                } else {
                    e.onNext(ImageUtils.getSmallImagePath(path))
                }
            }
            e.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .subscribe(object : Observer<String> {
                    internal var path: MutableList<String> = ArrayList()

                    override fun onSubscribe(d: Disposable) {
                        dimissLoadingDialog()
                        if (hasZipDialog) {
                            dialog = LoadingDialog.instance("处理中.,.")
                            dialog?.show(fragmentManager)
                        }
                    }

                    override fun onNext(s: String) {
                        path.add(s)
                    }

                    override fun onError(e: Throwable) {
                        listener.error()
                        dimissLoadingDialog()
                    }

                    override fun onComplete() {
                        dimissLoadingDialog()
                        listener.complete(path, identification)
                    }
                })

    }

    private fun startToCrop(inputString: String, options: UCrop.Options) {
        val path = PathUtils.PATH_CROP + Date()
                .time.toString() + ".jpg"
        val f = File(PathUtils.PATH_CROP)
        if (!f.exists()) {
            f.mkdirs()
        }
        val uCrop = UCrop.of(Uri.fromFile(File(inputString)), Uri.fromFile(File(path)))
        uCrop.withOptions(options)
        if (fragment != null) {
            uCrop.start(fragment!!.context!!, fragment!!, REQUEST_TAKE_PHOTO_CROP)
        } else {
            uCrop.start(activity!!, REQUEST_TAKE_PHOTO_CROP)
        }
    }

    private fun startActivity(intent: Intent, code: Int) {
        if (fragment != null) {
            fragment!!.startActivityForResult(intent, code)
        } else {
            activity!!.startActivityForResult(intent, code)
        }
    }

    /**
     * 销毁用于加载的对话框
     */
    private fun dimissLoadingDialog() {
        dialog?.let {

            if (it.dialog!=null&&it.dialog.isShowing) {
                it.dismiss()
                dialog = null
            }
        }
    }

    private fun getContext(): Context {
        return if (fragment != null) {
            fragment!!.activity!!
        } else {
            activity!!
        }
    }

    fun destroy() {
        dimissLoadingDialog()
        this.activity = null
        this.fragment = null
        this.listener = null
    }

    interface PictureResultListener {
        fun complete(list: List<String>, idtag: String?)

        fun error()
    }

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        /**
         * 拍照code
         */
        val REQUEST_TAKE_PHOTO = 20000

        /**
         * 裁图code
         */
        val REQUEST_TAKE_PHOTO_CROP = 20001

        /**
         * 相册列表code
         */
        val REQUEST_CHOSE_PICKTURE = 20002

        @JvmField
        val CREATOR: Parcelable.Creator<PictureHelper> = object : Parcelable.Creator<PictureHelper> {
            override fun createFromParcel(source: Parcel): PictureHelper = PictureHelper(source)
            override fun newArray(size: Int): Array<PictureHelper?> = arrayOfNulls(size)
        }
    }
}
