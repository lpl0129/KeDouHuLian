package com.stay.toolslibrary.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.githang.statusbar.StatusBarCompat
import com.stay.basiclib.R
import com.stay.toolslibrary.library.picture.PictureHelper
import com.stay.toolslibrary.library.refresh.PtrDefaultHandler
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.library.swaipLayout.SwipeBackActivity
import com.stay.toolslibrary.net.*
import com.stay.toolslibrary.utils.ActivityManagerUtils
import com.stay.toolslibrary.utils.PermissionsNameHelp
import com.stay.toolslibrary.utils.ToastUtils
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.widget.dialog.LoadingDialog
import com.stay.toolslibrary.widget.dialog.listener
import kotlinx.android.synthetic.main.base_include_view_loading.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.ArrayList

/**
 * @author huangyr
 * @date 2019/1/28 0028
 */

abstract class BaseActivity : SwipeBackActivity(), StatusViewManager.onRetryClick, EasyPermissions.PermissionCallbacks,
        View.OnClickListener, IListViewLisenerProvider {

    protected lateinit var topTitleTv: TextView
    protected lateinit var topLeftIv: ImageView
    protected lateinit var topView: FrameLayout
    protected lateinit var top_right: TextView
    protected lateinit var top_right_iv: ImageView
    private lateinit var statusViewManager: StatusViewManager

    private var pictureHelper: PictureHelper? = null

    fun getPictureHelper(): PictureHelper {
        if (pictureHelper == null) {
            pictureHelper = PictureHelper(this)
        }
        return pictureHelper!!
    }


    public var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivityToManager()
        val view = layoutInflater.inflate(getLayoutId(), null)
        setContentView(view)
        statusViewManager =
                StatusViewManager(this, view, getLoadingShow(), getErrorShow(), getEmptyShow(), getStatusViewMarginTop())
        statusViewManager.setOnRetryClick(object : StatusViewManager.onRetryClick {
            override fun onRetryLoad(errorMsgBean: NetMsgBean) {
                this@BaseActivity.onRetryLoad(errorMsgBean)
            }
        }
        )
        initViewClickListeners()
        setStatusBar()
        initBeforeListener()
        liveDataListener()
        processLogic(savedInstanceState)

    }

    /**
     * @描述 设置点击监听
     */
    open fun initViewClickListeners() {

    }

    open fun setStatusBar() {
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.whiteColor))
    }

    /**
     * @描述 加载布局文件
     */
    protected abstract fun getLayoutId(): Int

    /**
     * @描述 处理业务
     */
    protected abstract fun processLogic(savedInstanceState: Bundle?)

    override fun onClick(v: View?) {
    }

    protected fun setTopTitle(title: String) {
        topView = findViewById(R.id.top_view)
        topLeftIv = findViewById(R.id.top_left)
        topLeftIv.visibility = View.VISIBLE
        topTitleTv = findViewById(R.id.top_title)
        topTitleTv.text = title
        topLeftIv.setOnClickListener { finish() }
    }

    protected fun setTopTitle(title: String, rightText: String, action: () -> Unit) {
        top_right = findViewById(R.id.top_right)
        top_right.setVisibility(View.VISIBLE)
        top_right.setText(rightText)
        top_right.setOnClickListener {
            action()
        }
        setTopTitle(title)
    }

    protected fun setTopTitle(title: String, rightres: Int, action: () -> Unit) {
        top_right_iv = findViewById(R.id.top_iv_right)
        top_right_iv.visibility = View.VISIBLE
        top_right_iv.setImageResource(rightres)
        top_right_iv.setOnClickListener {
            action()
        }
        setTopTitle(title)
    }

    /**
     * 用于初始化adapter
     */
    open fun initBeforeListener() {

    }


    /**
     * 监听LieData
     */
    protected abstract fun liveDataListener()

    /**
     * 添加到Acitivity管理器
     */
    open protected fun addActivityToManager() {
        ActivityManagerUtils.getActivityManager().addActivity(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 权限统一
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    /**
     * 权限被拒绝
     */

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            val message = PermissionsNameHelp.getPermissionsMulti(perms)
            AppSettingsDialog.Builder(this).setTitle("权限申请")
                    .setRationale(message)
                    .setNegativeButton("暂不")
                    .setPositiveButton("设置")
                    .setThemeResId(R.style.AlertDialogTheme)
                    .build()
                    .show()
        }

    }

    /**
     * @param requestCode
     * @param strings
     */
    fun requsetPerMission(requestCode: Int, strings: List<String>) {
        val perms = hasPermissions(this, strings)
        if (perms == null) {
            PerMissionSuccess(requestCode)
        } else {
            EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, requestCode, *perms)
                            .setRationale(PermissionsNameHelp.getPermissionsMulti(strings))
                            .setPositiveButtonText("设置")
                            .setNegativeButtonText("暂不")
                            .setTheme(R.style.AlertDialogTheme)
                            .build()
            )
        }
    }

    private fun PerMissionSuccess(requestCode: Int) {
        val permissions = arrayOf("SUCCESS")
        val grantResults = IntArray(1)
        grantResults[0] = PackageManager.PERMISSION_GRANTED
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun hasPermissions(context: Context?, perms: List<String>): Array<String>? {
        var strings: MutableList<String>? = null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null
        }
        if (context == null) {
            throw IllegalArgumentException("Can't check permissions for null context")
        }
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                if (strings == null) {
                    strings = ArrayList()
                }
                strings.add(perm)
            }
        }
        return if (strings != null && !strings.isEmpty()) {
            strings.toTypedArray()
        } else null
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {

    }

    fun setLoadingDilogText(msg: String) {
        if (loadingDialog != null) {
            loadingDialog!!.setLoadingText(msg)
        } else {
            showLoadingDilog(msg)
        }
    }

    fun showLoadingDilog(msg: String) {
        dimissLoadingDialog()
        loadingDialog = LoadingDialog.instance(msg)
        loadingDialog?.show(supportFragmentManager)
    }

    fun showToast(text: String) {
        ToastUtils.showShort(text)
    }

    /**
     * 销毁用于加载的对话框
     */
    fun dimissLoadingDialog() {
//        loadingDialog.dismiss()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     *
     */
    protected open fun getStatusViewMarginTop(): Int {
        return 45.dp2px()
    }

    protected fun getErrorShow(): Int {
        return R.layout.base_include_view_error
    }

    protected fun getLoadingShow(): Int {
        return R.layout.base_include_view_loading
    }

    protected fun getEmptyShow(): Int {
        return R.layout.base_include_view_empty
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (pictureHelper != null) {
            outState.putParcelable("pictureHelper", pictureHelper)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val helper = savedInstanceState.getParcelable<Parcelable>("pictureHelper")
        helper?.let {

            pictureHelper?.destroy()
            pictureHelper = it as PictureHelper
            it.activity = this
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        statusViewManager.onDestory()
        ActivityManagerUtils.getActivityManager().finishActivity(this)
        pictureHelper?.let {
            it.destroy()
            pictureHelper = null
        }

        super.onDestroy()
    }

    fun <T : NetMsgBeanProvider> LiveData<T>.toObservable(owner: LifecycleOwner, block: (T) -> Unit) {
        this.observe(owner, Observer<T> { t ->
            if (t != null) {
                dimissLoadingDialog()
                var msgBean = t.getNetMsgBean()
                when (msgBean.status) {

                    ViewStatus.LOADING_NO -> {

                    }
                    ViewStatus.LOADING_DIALOG -> {
                        showLoadingDilog(msgBean.loadingMsg)
                    }
                    ViewStatus.ERROR_NO -> {

                    }
                    ViewStatus.ERROR_TOAST ->
                        showToast(msgBean.errorMsg)

                    ViewStatus.SUCCESS -> {
                        block(t)
                        statusViewManager.showStatusView(msgBean)
                    }
                    else -> {
                        statusViewManager.showStatusView(msgBean)
                    }
                }

            }
        })
    }

    fun SingleLiveEvent<NetListManager>.toObservableList(owner: LifecycleOwner, provider: IListViewProvider) {

        val ptr = provider.ptrProvider
        val recyclerView = provider.recyclerViewProvider
        val adpater = provider.getRecyclerAdapter()
        val layoutManger = provider.getLayoutManager()

        var listener: RequestListenerBuilder? = null

        recyclerView.layoutManager = layoutManger
        recyclerView.adapter = adpater

        listener = provider.getListenerBuilder()

        ptr.setPtrHandler(object : PtrDefaultHandler() {

            override fun onRefreshBegin(frame: PtrFrameLayout) {
                provider.ptrRequestListener()
                listener?.mrequestBeaginAction?.let {
                    it(true)
                }
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout, content: View?, header: View?): Boolean {
                return if (listener?.mcheckCanRefreshAction == null) {
                    super.checkCanDoRefresh(frame, content, header)
                } else {
                    listener.mcheckCanRefreshAction!!()
                }
            }
        }
        )
        ptr.setOnLoadMoreListener {

            provider.ptrRequestListener(false)
            listener?.mrequestBeaginAction?.let {
                it(false)
            }
        }

        this.observe(owner, Observer {
            it?.let { manager ->
                with(manager)
                {
                    when (status) {
                        NetListListenerStatus.REQUEST_SUCCESS -> {
                            adpater.isInitEmpty = false
                            if (isRefresh) {
                                ptr.refreshComplete()
                                if (!isEmpty) {
                                    ptr.isLoadMoreEnable = true
                                    ptr.loadMoreComplete(hasMore)
                                } else {
                                    adpater.isError = false
                                    ptr.loadMoreComplete(false)
                                    ptr.loadComplete()
                                }
                                adpater.notifyDataSetChanged()
                            } else {
                                if (!isEmpty) {
                                    adpater.notifyDataSetChanged()
                                    ptr.loadMoreComplete(hasMore)
                                } else {
                                    ptr.loadMoreComplete(false)
                                }
                            }
                            listener?.mrequestSuccessAction?.let {
                                it(isRefresh)
                            }
                        }
                        NetListListenerStatus.REQUEST_FAILED -> {
                            adpater.isInitEmpty = false
                            if (isRefresh) {
                                ptr.refreshComplete()
                            }
                            if (isEmpty) {
                                adpater.isError = true
                                adpater.setErrorBindListener {

                                    mOnBindErrorListener {

                                        val error_tv = findViewById<TextView>(com.stay.basiclib.R.id.error_tv)
                                        val error_iv = findViewById<ImageView>(com.stay.basiclib.R.id.error_iv)
                                        val error_bt = findViewById<TextView>(com.stay.basiclib.R.id.error_bt)
                                        error_tv.text = netMsgBean.errorMsg
                                        if (netMsgBean.errorImageRes == 0) {
                                            error_iv.visibility = android.view.View.GONE
                                        } else {
                                            error_iv.visibility = android.view.View.VISIBLE
                                            error_iv.setImageResource(netMsgBean.errorImageRes)
                                        }
                                        error_bt.setOnClickListener {
                                            ptr.autoRefresh()
                                        }
                                    }
                                }
                                adpater.notifyDataSetChanged()
                                ptr.loadComplete()
                            } else {
                                ptr.setErrorData()
                            }
                            listener?.mrequestFailedAction?.let {
                                it(isRefresh)
                            }
                        }
                        else -> {

                        }
                    }
                }
            }

        })
    }

    override fun getListenerBuilder(): RequestListenerBuilder? {
        return null
    }

}
