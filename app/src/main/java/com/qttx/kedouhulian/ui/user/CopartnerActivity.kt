package com.qttx.kedouhulian.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.user.adapter.CopartnerAdapter
import com.qttx.kedouhulian.ui.user.viewModel.UserCopartnerViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.net.RequestListenerBuilder
import com.stay.toolslibrary.utils.ClipboardUtils
import com.stay.toolslibrary.utils.extension.loadImage
import com.stay.toolslibrary.utils.livedata.bindScheduler
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.user_activity_copartner.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.FileNotFoundException

/**
 * @author huangyr
 * @date 2019/5/18 0018
 */
class CopartnerActivity : BaseActivity(), IListViewProvider {


    private val listModel: UserCopartnerViewModel by viewModel()
    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView

    private lateinit var adapter: CopartnerAdapter
    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int {
        return R.layout.user_activity_copartner
    }

    override fun ptrRequestListener(isRefresh: Boolean) {
        val map = mutableMapOf<String, String>()
        if (isRefresh) {
            listModel.getInfo(this)

        }
        listModel.getListData(this, isRefresh, map)
    }

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(this)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = CopartnerAdapter(listModel.list)
    }

    override fun liveDataListener() {
        listModel.listManagerLiveData.toObservableList(this, this)
        listModel.copartenerNetLiveData.toObservable(this)
        {
            it.data?.let {

                partnercountTv.text = "合伙人总数量：${it.partnercount}人"
                vpartnercountTv.text = "有效合伙人数量：${it.vpartnercount}人"
                incomeTv.text = "${it.accumulated_earnings}元"
                qrcodeIv.loadImage(it.QRcode)
                qrcodeUrlTv.text = it.link
                inviteCode.text = it.invite_code
            }
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("我的合伙人")
        scrollableLayout.helper
                .setCurrentScrollableContainer {
                    return@setCurrentScrollableContainer recyclerView
                }

        ptrRequestListener(true)

    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun getListenerBuilder(): RequestListenerBuilder? {
        return RequestListenerBuilder().apply {
            checkCanRefresh {
                scrollableLayout.canPtr()
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            saveQrcodeTv -> {
                listModel
                        .copartenerNetLiveData
                        .value?.data?.let { bean ->
                    Observable.create<String> {

                        val path = Glide.with(this)
                                .downloadOnly()
                                .load(bean.QRcode)
                                .submit()
                                .get()
                                .absolutePath
                        try {
                            MediaStore.Images.Media.insertImage(contentResolver,
                                    path, "kedou_code", null)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                        // 最后通知图库更新
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
                        it.onNext(path);
                        it.onComplete()
                    }.bindScheduler()
                            .subscribe(object : Consumer<String> {
                                override fun accept(t: String?) {
                                    showToast("保存成功")
                                }
                            }
                            )


                }
            }
            copyQrcodeUrlTv -> {
                listModel
                        .copartenerNetLiveData
                        .value?.data?.let {
                    ClipboardUtils.copyText(it.link)
                    showToast("已保存到粘贴板")
                }
            }
            copyInviteCode -> {
                listModel
                        .copartenerNetLiveData
                        .value?.data?.let {
                    ClipboardUtils.copyText(it.invite_code)
                    showToast("已保存到粘贴板")
                }
            }
        }
    }

    override fun initViewClickListeners() {
        saveQrcodeTv.setOnClickListener(this)
        copyQrcodeUrlTv.setOnClickListener(this)
        copyInviteCode.setOnClickListener(this)
    }
}