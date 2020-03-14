package com.qttx.kedouhulian.ui.reward.redpacket

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.OssBean
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.bean.SystemConfig
import com.qttx.kedouhulian.receiver.UpLoadToOssServices
import com.qttx.kedouhulian.room.DataBase
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.dialog.ChoseCityDilog
import com.qttx.kedouhulian.ui.reward.redpacket.viewModel.RedPacketPublishViewModel
import com.qttx.kedouhulian.utils.ExtractVideoInfoUtil
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.*
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.utils.extension.loadImage
import com.stay.toolslibrary.utils.extension.px2dp
import com.stay.toolslibrary.utils.extension.toArrayList
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.filter.GifSizeFilter
import com.zhihu.matisse.filter.VideoSizeFilter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.reward_activity_red_packet_publish.*
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.ArrayList


class RedPacketPublishActivity : BaseActivity() {
    /**
     * 所有资源附件合计
     */
    private var ossList = mutableListOf<OssBean>()
    /**
     * 视频资源
     */
    private var videoBean: OssBean? = null
    /**
     * 视频缩略图资源
     */
    private var thumbBean: OssBean? = null

    private var upLoadToOssServices: UpLoadToOssServices? = null
    private var hasBindService: Boolean = false

    private val viewModel: RedPacketPublishViewModel by viewModel()

    private val startList = ArrayList<RegionsBean>()

    private var startParentCity: RegionsBean? = null

    private var info: SystemConfig? = null

    override fun getLayoutId(): Int = R.layout.reward_activity_red_packet_publish

    private var heightBar: Int = 0

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("发布广告")
        viewModel.getAppConfig(this)
        initViewState()
        val intent = Intent(this, UpLoadToOssServices::class.java)
        hasBindService = bindService(intent, conn, Context.BIND_AUTO_CREATE)//绑定目标Service

        heightBar = ScreenUtils.getScreenHeight() - 45.dp2px() - StatusBarUtils.getStatusBarHeight()
        heightBar = heightBar.px2dp().toInt()
        taskAcountEt.keyListener = DigitsKeyListener.getInstance("1234567890")
        timeEt.keyListener = DigitsKeyListener.getInstance("1234567890")


        timeEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val text = s.toString()
                    val len = s.toString().length
                    if (len == 1 && text.startsWith("0")) {
                        it.replace(0, 1, "")
                    }

                    if (text.isNotEmpty() && !text.startsWith(".")) {
                        val price = java.lang.Double.parseDouble(text)
                        if (price > 15) {
                            timeEt.setText("15")
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

        )

        taskAcountEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val text = s.toString()
                    val len = s.toString().length
                    if (len == 1 && text.startsWith("0")) {
                        it.replace(0, 1, "")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

        )
        taskPriceEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val text = s.toString()
                    val len = s.toString().length
                    if (len == 1 && text.startsWith(".")) {
                        it.replace(0, 1, "")
                    } else if (len == 2 && text.startsWith("0") && !text.contains(".")) {
                        it.replace(0, 1, "")
                    } else {
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setPrice()
            }
        }
        )


    }

    override fun liveDataListener() {
        viewModel.appConfig
                .toObservable(this)
                {
                    info = it.data
                    isTopTv.text = "置顶${it.data?.red_top}/次"
                }
        viewModel.tokenLiveData.toObservable(this)
        {
            //上传
            it.data?.let {
                uploadFile(it)
            }
        }
        viewModel.saveLiveData.toObservable(this)
        {

            PayActivity.newInstance(it.data!!)
                    .setListener(PayResult {
                        setResult(400)
                        finish()
                    }
                    )
                    .show(supportFragmentManager)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            peituIV -> {
                requsetPerMission(10103, listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
            }
            oneDayOneTimeTv -> {
                viewModel.onceType = 0
                initViewState()
            }
            oneTimeTv -> {
                viewModel.onceType = 1
                initViewState()
            }
            sexLimitTv -> {
                viewModel.isSexLimit = 0
                initViewState()
            }
            sexBoyEableTv -> {
                viewModel.isSexLimit = 1
                initViewState()
            }
            sexGirlEableTv -> {
                viewModel.isSexLimit = 2
                initViewState()
            }
            ageLimt0Tv -> {
                viewModel.ageLimit.clear()
                viewModel.ageLimit.add(0)
                initViewState()
            }
            ageLimt1Tv -> {

                if (viewModel.ageLimit.contains(0)) {
                    viewModel.ageLimit.remove(0)
                }
                if (!viewModel.ageLimit.contains(1)) {
                    viewModel.ageLimit.add(1)
                }
                initViewState()
            }
            ageLimt2Tv -> {
                if (viewModel.ageLimit.contains(0)) {
                    viewModel.ageLimit.remove(0)
                }
                if (!viewModel.ageLimit.contains(2)) {
                    viewModel.ageLimit.add(2)
                }
                initViewState()
            }
            ageLimt3Tv -> {
                if (viewModel.ageLimit.contains(0)) {
                    viewModel.ageLimit.remove(0)
                }
                if (!viewModel.ageLimit.contains(3)) {
                    viewModel.ageLimit.add(3)
                }
                initViewState()
            }
            shouqiRedPacketTv -> {
                viewModel.redPacketModel = 1
                initViewState()
            }
            putongRedPacketTv -> {
                viewModel.redPacketModel = 0
                initViewState()
            }
            regionLimitLl -> {
                ChoseCityDilog
                        .newInstance(heightBar, startList, startParentCity, 0, 100)
                        .setStateListenerListener {

                        }
                        .setSelectListener { list, bean ->
                            startList.clear()
                            startList.addAll(list)
                            startParentCity = bean
                            if (startList.isEmpty()) {
                                regionLimitTv.text = ""
                            } else {
                                val startbuilder = StringBuilder()
                                startList.forEach {
                                    if (it.id != 0) {
                                        startbuilder.append(it.name)
                                                .append(",")
                                    }
                                }
                                if (TextUtils.isEmpty(startbuilder)) {
                                    regionLimitTv.text = ""
                                } else {
                                    regionLimitTv.text = startbuilder.deleteCharAt(startbuilder.length - 1).toString()
                                }
                            }

                        }.show(supportFragmentManager)
            }
            noTopTv -> {
                viewModel.isTop = 0
                initViewState()
                topPriceTv.text = ""
            }
            isTopTv -> {
                viewModel.isTop = 1
                initViewState()
                topPriceTv.text = info?.red_top.toString()
            }
            publishTv -> {
                if (checkData()) {
                    if (thumbBean == null) {
                        getArea()
                    } else {

                        val token = viewModel.tokenLiveData.value?.data
                        if (token.isNullOrEmpty()) {
                            viewModel.getToken(this)
                        } else {
                            uploadFile(token)
                        }


                    }

                }
            }
        }
    }

    fun uploadFile(token: String) {
        /**
         * 所有资源附件合计
         */
        ossList.clear()
        thumbBean?.let {
            ossList.add(it)
        }
        videoBean?.let {
            ossList.add(it)
        }
        showLoadingDilog("正在上传...")
        val intent = Intent(this, UpLoadToOssServices::class.java)
        intent.putExtra("list", ossList.toArrayList())
        intent.putExtra("token", token)
        startService(intent)
    }

    override fun initViewClickListeners() {
        peituIV.setOnClickListener(this)
        oneDayOneTimeTv.setOnClickListener(this)
        oneTimeTv.setOnClickListener(this)
        sexLimitTv.setOnClickListener(this)
        sexBoyEableTv.setOnClickListener(this)
        sexGirlEableTv.setOnClickListener(this)
        ageLimt0Tv.setOnClickListener(this)
        ageLimt1Tv.setOnClickListener(this)
        ageLimt2Tv.setOnClickListener(this)
        ageLimt3Tv.setOnClickListener(this)
        shouqiRedPacketTv.setOnClickListener(this)
        putongRedPacketTv.setOnClickListener(this)
        regionLimitLl.setOnClickListener(this)
        noTopTv.setOnClickListener(this)
        isTopTv.setOnClickListener(this)
        publishTv.setOnClickListener(this)
    }

    private fun initViewState() {

        sexLimitTv.isSelected = viewModel.isSexLimit == 0
        sexBoyEableTv.isSelected = viewModel.isSexLimit == 1
        sexGirlEableTv.isSelected = viewModel.isSexLimit == 2
        isTopTv.isSelected = viewModel.isTop == 1
        noTopTv.isSelected = viewModel.isTop == 0
        oneDayOneTimeTv.isSelected = viewModel.onceType == 0
        oneTimeTv.isSelected = viewModel.onceType == 1
        ageLimt0Tv.isSelected = viewModel.ageLimit.contains(0)
        ageLimt1Tv.isSelected = viewModel.ageLimit.contains(1)
        ageLimt2Tv.isSelected = viewModel.ageLimit.contains(2)
        ageLimt3Tv.isSelected = viewModel.ageLimit.contains(3)
        shouqiRedPacketTv.isSelected = viewModel.redPacketModel == 1
        putongRedPacketTv.isSelected = viewModel.redPacketModel == 0
    }

    private fun setPrice() {
        val rate = info?.red_fee
        val taskPrice = taskPriceEt.text.toString()
        if (taskPrice.isEmpty()) {
            taskPriceTv.text = "0"
            serverPriceTv.text = "0"
        } else {
            val serverPrice = BigDecimalUtils.mul(taskPrice, rate.toString(), 2)
            taskPriceTv.text = taskPrice
            serverPriceTv.text = serverPrice
        }
    }


    @AfterPermissionGranted(10103)
    fun onCramPre() {
        PathUtils.initPath(PinyinUtils.ccs2Pinyin(resources.getString(com.stay.basiclib.R.string.app_name)))
        choseMedia(MimeType.ofAll(), 1, true)
    }

    /**
     * 视频列表code
     */
    val REQUEST_CHOSE_VIDEO = 10002

    private fun choseMedia(mimeTypes: Set<MimeType>, size: Int, hasCarme: Boolean) {
        val matisse = Matisse.from(this@RedPacketPublishActivity)
        val selectionCreator = matisse.choose(mimeTypes, true)
                .countable(true)
                .capture(hasCarme)
                .captureStrategy(
                        CaptureStrategy(true, this.getPackageName() + ".FileProvider"))
                .showSingleMediaType(true)
                .maxSelectable(size)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .addFilter(VideoSizeFilter(500, 100 * Filter.K * Filter.K))
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
        selectionCreator.forResult(REQUEST_CHOSE_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHOSE_VIDEO && resultCode == Activity.RESULT_OK && data != null) {
            val datalist = Matisse.obtainResult(data)
            if (!EmptyUtils.isEmpty(datalist)) {
                val videoItem = datalist[0]
                if (videoItem.isVideo) {
                    thumbBean = null
                    videoBean = null
                    val path = com.zhihu.matisse.internal.utils.PathUtils.getPath(this@RedPacketPublishActivity, videoItem.contentUri)
                    Observable.just(videoItem)
                            .map {
                                val videoInfoUtil = ExtractVideoInfoUtil(path)
                                videoInfoUtil.extractFrames(PathUtils.PATH_IMAGE)
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<String> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onNext(s: String) {
                                    thumbBean = OssBean()
                                    thumbBean!!.localUrl = s
                                    thumbBean!!.type = OssBean.TYPE.THUMB
                                    peituIV.loadImage(thumbBean!!.localUrl)
                                    videoBean = OssBean()
                                    videoBean!!.type = OssBean.TYPE.VIDEO
                                    videoBean!!.localUrl = path
                                    val length = videoItem.duration
                                    videoBean!!.length = length
                                    videoBean!!.size = videoItem.size
                                }

                                override fun onError(e: Throwable) {

                                }

                                override fun onComplete() {

                                }
                            })
                } else {
                    val list = Matisse.obtainPathResult(data)
                    thumbBean = OssBean()
                    thumbBean!!.localUrl = list[0]
                    thumbBean!!.type = OssBean.TYPE.THUMB
                    peituIV.loadImage(thumbBean!!.localUrl)
                }
            }
        }
    }


    private fun checkData(): Boolean {
        val title = taskTitleTv.text.toString()
        if (title.isEmpty()) {
            showToast("请输入广告标题")
            return false
        }
        val content = taskContentTv.text.toString()

        if (content.isEmpty()) {
            showToast("请输入广告内容")
            return false
        }
        val link = taskLinkEt.text.toString()
        if (link.isNotEmpty() && !link.startsWith("http")) {

            showToast("链接内容必须以http开头")
            return false
        }

        val acount = taskAcountEt.text.toString()
        if (acount.isEmpty()) {
            showToast("请输入红包数量")
            return false
        }

        val price = taskPriceEt.text.toString()
        if (price.isEmpty()) {
            showToast("请输入红包总额")
            return false
        }
        if (price.toDouble() <= 0) {
            showToast("红包总额不能小于0")
            return false
        }
        val div = BigDecimalUtils.mul(acount.toDouble(), 0.01)
        if (price.toDouble() < div) {
            showToast("平均每人不能低于0.01元")
            return false
        }

        val time = timeEt.text.toString()
        if (time.isEmpty()) {
            showToast("请输入领取红包时间")
            return false
        }

        if (time.toInt() > 15) {
            showToast("领取红包时间不能大于15s")
            return false
        }
        val kouLing = kouLingTv.text.toString()
        if (kouLing.isEmpty()) {
            showToast("请输入领取红包口令")
            return false
        }
        return true

    }

    private fun getArea() {

        if (regionLimitTv.text.isNullOrEmpty()) {
            //无限制
            saveData(null)
        } else {
            //获取限制
            Observable.just(startList)
                    .map {
                        val cityList = mutableListOf<RegionsBean>()

                        val areaList = mutableListOf<RegionsBean>()
                        it.forEach {
                            if (it.level == 1) {
                                val city = DataBase.getInstance(this@RedPacketPublishActivity)
                                        .cityDao()
                                        .getCityByPid(it.id)
                                cityList.addAll(city)
                            } else if (it.level == 2) {
                                cityList.add(it)
                            } else {
//                                areaList.add(it)
                                cityList.add(it)
                            }
                        }

//                        cityList.forEach {
//                            val city = DataBase.getInstance(this@RewardTaskPublishActivity)
//                                    .cityDao()
//                                    .getCityByCityCode(it.citycode)
//                            areaList.addAll(city)
//                        }
                        val builder = StringBuilder()
                        cityList.forEach {
                            //                            if (it.level == 3) {
                            builder.append(it.id)
                                    .append(",")
//                            }
                        }
                        builder.deleteCharAt(builder.length - 1).toString()
                    }
                    .bindScheduler()
                    .bindLifecycle(this)
                    .subscribe {
                        saveData(it)
                    }
        }
    }

    private fun saveData(area_id: String?) {
        val params = mutableMapOf<String, String>()


        params["title"] = taskTitleTv.text.toString()

        params["content"] = taskContentTv.text.toString()

        ossList.forEach {

            if (it.type == OssBean.TYPE.THUMB) {
                params["imgs"] = it.serverUrl
            } else if (it.type == OssBean.TYPE.VIDEO) {
                params["video_url"] = it.serverUrl
                params["video_length"] = it.length.toString()
            }
        }

        val link = taskLinkEt.text.toString()
        if (link.isNotEmpty()) {
            params["link"] = link
        }

        params["total_num"] = taskAcountEt.text.toString()

        params["total_price"] = taskPriceEt.text.toString()

        params["waite_time"] = timeEt.text.toString()
        params["red_pass"] = kouLingTv.text.toString()
        params["limit_num"] = viewModel.onceType.toString()

        params["limit_sex"] = viewModel.isSexLimit.toString()

        params["limit_age"] = viewModel.ageLimit.let {
            val builder = StringBuilder()
            it.forEach {
                builder.append(it.toString())
                        .append(",")
            }
            builder.deleteCharAt(builder.length - 1).toString()
        }
        if (area_id == null) {
            params["limit_area"] = "0"

        } else {
            params["limit_area"] = area_id
        }
        params["red_type"] = viewModel.redPacketModel.toString()

        params["is_top"] = viewModel.isTop.toString()
        viewModel.saveData(this, params)
    }

    private val conn = object : ServiceConnection {
        /** 获取服务对象时的操作  */
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as UpLoadToOssServices.MyBinder
            upLoadToOssServices = service.service
            binder?.let {
                binder.setListener(object : UpLoadToOssServices.UploadListener {
                    override fun onUploadSuccess(list: ArrayList<OssBean>) {
                        ossList = list
                        getArea()
                    }

                    override fun onUploadFailed() {

                        dimissLoadingDialog()
                        showToast("上传失败")
                    }
                })
            }

        }

        /** 无法获取到服务对象时的操作  */
        override fun onServiceDisconnected(name: ComponentName) {
            LogUtils.e("dsd", name.toString())
        }
    }

    override fun onDestroy() {
        if (hasBindService) {
            unbindService(conn)
        }
        upLoadToOssServices?.let {
            it.listener = null
            it.isStopUpload = true
            it.stopSelf()
        }
        dimissLoadingDialog()
        super.onDestroy()
    }
}
