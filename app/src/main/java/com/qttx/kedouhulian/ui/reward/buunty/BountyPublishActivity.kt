package com.qttx.kedouhulian.ui.reward.buunty

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import com.lxj.xpopup.XPopup
import com.qiniu.android.storage.UploadManager
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.HunterConfig
import com.qttx.kedouhulian.bean.ImageBean
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.room.DataBase
import com.qttx.kedouhulian.ui.common.PayActivity
import com.qttx.kedouhulian.ui.common.PayResult
import com.qttx.kedouhulian.ui.dialog.ChoseCityDilog
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.BountyPublishViewModel
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.library.picture.PictureHelper
import com.stay.toolslibrary.utils.BigDecimalUtils
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.ScreenUtils
import com.stay.toolslibrary.utils.StatusBarUtils
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.utils.extension.px2dp
import com.stay.toolslibrary.utils.livedata.bindLifecycle
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.widget.UploadImageLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.reward_activity_publish_bounty.*
import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File
import java.util.*

/**
 * @author huangyr
 * 发布赏金
 * @date 2019/5/5 0005
 */
class BountyPublishActivity : BaseActivity() {


    private val viewModel: BountyPublishViewModel by viewModel()

    private var heightBar: Int = 0
    private lateinit var hunterConfig: HunterConfig

    private var listSelect = -1

    private var unitSelect = "天"

    private val startList = ArrayList<RegionsBean>()

    private var startParentCity: RegionsBean? = null


    private val imageList = ArrayList<ImageBean>()
    private var uploadManager: UploadManager? = null

    override fun getLayoutId(): Int = R.layout.reward_activity_publish_bounty

    override fun liveDataListener() {
        viewModel.configLiveData
                .toObservable(this)
                {
                    it.data?.let {
                        hunterConfig = it
                        unitSelect = it.unitlist[0]
                        taskPriceUnitTv.text = "/$unitSelect"
                        isTopTv.text = "置顶${hunterConfig.top_price}/次"
                    }
                }

          viewModel.tokenLiveData.toObservable(this)
        {
            //上传
            it.data?.let {
                imageList.clear()
                taskImageLayout.imageList.forEach {
                    val bean = ImageBean()
                    bean.local = it
                    imageList.add(bean)
                }
                val beginImageSize = imageList.size

                upLoadImage(beginImageSize, it)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getPictureHelper().getPhotoList(requestCode, resultCode, data, object : PictureHelper.PictureResultListener {
            override fun error() {
            }

            override fun complete(list: List<String>, idtag: String?) {
                if (!list.isEmpty()) {
                    taskImageLayout.addImageList(list)
                }
            }
        })
    }
    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("发布悬赏")
        viewModel.getData(this)
        heightBar = ScreenUtils.getScreenHeight() - 45.dp2px() - StatusBarUtils.getStatusBarHeight()
        heightBar = heightBar.px2dp().toInt()
        initViewState()
        taskAcountEt.keyListener = DigitsKeyListener.getInstance("1234567890")
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
                setPrice()
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
        taskImageLayout.addImageList(listOf())
        taskImageLayout.setUploadManager(object : UploadImageLayout.onUploadManager {
            override fun onDelete(parentPos: Int, childrenPos: Int) {

            }

            override fun onSelect(parentPos: Int, limitsize: Int) {
                viewModel.limitsize = limitsize
                val per = listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requsetPerMission(10001, per)
            }
        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            taskTypeLl -> {
                val titleList = mutableListOf<String>()
                hunterConfig.catelist.forEach {
                    titleList.add(it.type_name)
                }
                XPopup.Builder(this)
                        .asBottomList("悬赏类型", titleList.toTypedArray(), null, listSelect)
                        { position, text ->
                            taskTypeTv.text = text
                            listSelect = position
                        }.show()
            }
            taskPriceUnitLl -> {
                XPopup.Builder(this)
                        .asBottomList("规格",hunterConfig.unitlist.toTypedArray(), null, -1)
                        { position, text ->
                            taskPriceUnitTv.text = text
                            unitSelect = text
                        }.show()
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
            imageShowTv -> {
                viewModel.isImageShow = 0
                initViewState()
            }
            imageHideenTv -> {
                viewModel.isImageShow = 1
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
            isTopTv -> {
                viewModel.isTop = 1
                initViewState()
                topPriceTv.text = hunterConfig.top_price.toString()
            }
            noTopTv -> {
                viewModel.isTop = 0
                initViewState()
                topPriceTv.text = ""
            }
            publishTv -> {
                if (chekData()) {
                    if (taskImageLayout.imageList.isEmpty()) {
                        getArea()
                    } else {
                        val token = viewModel.tokenLiveData.value?.data
                        if (token.isNullOrEmpty()) {
                            viewModel.getToken(this)
                        } else {
                            //上传
                            imageList.clear()
                            taskImageLayout.imageList.forEach {
                                val bean = ImageBean()
                                bean.local = it
                                imageList.add(bean)
                            }
                            val beginImageSize = imageList.size
                            upLoadImage(beginImageSize, token)
                        }
                    }
                }
            }
        }
    }

    override fun initViewClickListeners() {
        taskTypeLl.setOnClickListener(this)
        taskPriceUnitLl.setOnClickListener(this)
        sexLimitTv.setOnClickListener(this)
        sexBoyEableTv.setOnClickListener(this)
        sexGirlEableTv.setOnClickListener(this)
        imageShowTv.setOnClickListener(this)
        imageHideenTv.setOnClickListener(this)
        regionLimitLl.setOnClickListener(this)
        isTopTv.setOnClickListener(this)
        noTopTv.setOnClickListener(this)
        publishTv.setOnClickListener(this)
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

    private fun setPrice() {
        val rate = hunterConfig.bounty_user_fee
        val account = taskAcountEt.text.toString()
        val taskPrice = taskPriceEt.text.toString()
        if (account.isEmpty() || taskPrice.isEmpty()) {
            taskPriceTv.text = "0"
            serverPriceTv.text = "0"
        } else {
            val priceAll = BigDecimalUtils.mul(account, taskPrice, 2)
            val serverPrice = BigDecimalUtils.mul(priceAll, rate.toString(), 2)
            taskPriceTv.text = priceAll
            serverPriceTv.text = serverPrice
        }
    }

    private fun initViewState() {
        imageHideenTv.isSelected = viewModel.isImageShow == 1
        imageShowTv.isSelected = viewModel.isImageShow == 0
        sexLimitTv.isSelected = viewModel.isSexLimit == 0
        sexBoyEableTv.isSelected = viewModel.isSexLimit == 1
        sexGirlEableTv.isSelected = viewModel.isSexLimit == 2
        isTopTv.isSelected = viewModel.isTop == 1
        noTopTv.isSelected = viewModel.isTop == 0
    }

    private fun chekData(): Boolean {
        if (listSelect == -1) {
            showToast("请选择悬赏类型")
            return false
        }
        val title = taskTitleTv.text.toString()
        if (title.isEmpty()) {
            showToast("请输入悬赏标题")
            return false
        }
        val content = taskContentTv.text.toString()

        if (content.isEmpty()) {
            showToast("请输入悬赏内容")
            return false
        }

        val imageList = taskImageLayout.imageList
        if (imageList.isEmpty()) {
            showToast("请上传悬赏配图")
            return false
        }
        val account = taskAcountEt.text.toString()
        if (account.isEmpty()) {
            showToast("请输入需求人数")
            return false
        }

        val taskPrice = taskPriceEt.text.toString()
        if (taskPrice.isEmpty()) {
            showToast("请输入赏金金额")
            return false
        }
        return true
    }

    private fun upLoadImage(beginsize: Int, token: String) {
        var beginsize = beginsize
        if (loadingDialog == null || loadingDialog?.dialog == null) {
            showLoadingDilog("正在上传...")
        }
        beginsize -= 1
        if (beginsize >= 0) {
            val localUrl = imageList[beginsize].local
            val maxfile = File(localUrl)
            if (!maxfile.exists() || !maxfile.isFile()) {
                imageList.removeAt(beginsize)
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
                            imageList[finalBeginsize].img = key
                        } else {
                            imageList.removeAt(finalBeginsize)
                        }
                        upLoadImage(finalBeginsize, token)
                    }, null)

        } else {
            dimissLoadingDialog()
            //开始提交后台数据
            if (chekData()) {
                getArea()
            }
        }
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
                                val city = DataBase.getInstance(this@BountyPublishActivity)
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

        params["cid"] = hunterConfig.catelist[listSelect].id.toString()

        params["title"] = taskTitleTv.text.toString()

        params["content"] = taskContentTv.text.toString()

        val builder = StringBuilder()

//        //图片非必传
//        if (!imageList.isEmpty()) {
        imageList.forEach {
            builder.append(it.img)
                    .append(",")
        }
        if (builder.isEmpty()) {
            showToast("任务配图上传失败,请重新上传")
            return
        } else {
            params["imgs"] = builder.deleteCharAt(builder.length - 1).toString()
        }
//        }

        params["total_num"] = taskAcountEt.text.toString()

        params["price"] = taskPriceEt.text.toString()


        params["unit"] = unitSelect

        params["hidden_img"] = viewModel.isImageShow.toString()

        params["limit_sex"] = viewModel.isSexLimit.toString()

        if (area_id == null) {
            params["limit_area"] = "0"

        } else {
            params["limit_area"] = area_id
        }
        if (viewModel.isTop==1)
        {
            params["top_price"] = hunterConfig.top_price.toString()
        }
        params["is_top"] = viewModel.isTop.toString()
        viewModel.saveData(this, params)
    }
}