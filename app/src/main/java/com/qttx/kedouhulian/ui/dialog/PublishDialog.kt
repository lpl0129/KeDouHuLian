package com.qttx.kedouhulian.ui.dialog

import android.content.Context
import android.view.View
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.R

/**
 * @author huangyr
 * @date 2019/4/16 0016
 */
class PublishDialog constructor(mContext: Context) : AttachPopupView(mContext) {

    private var selectListener: OnSelectListener? = null

    override fun getImplLayoutId(): Int {
        return R.layout.reward_pop_publish

    }

    override fun onCreate() {
        super.onCreate()
        findViewById<View>(R.id.publishTask).setOnClickListener {
            selectListener?.onSelect(0, "发布")
            if (popupInfo.autoDismiss) dismiss()
        }
        findViewById<View>(R.id.myTask).setOnClickListener {
            selectListener?.onSelect(1, "我的")
            if (popupInfo.autoDismiss) dismiss()
        }

        findViewById<View>(R.id.taskSet).setOnClickListener {
            selectListener?.onSelect(2, "任务设置")
            if (popupInfo.autoDismiss) dismiss()
        }

    }

    fun setOffsetXAndY(offsetX: Int, offsetY: Int): PublishDialog {
        this.defaultOffsetX += offsetX
        this.defaultOffsetY += offsetY
        return this
    }
    fun setOnSelectListener(selectListener: OnSelectListener): PublishDialog {
        this.selectListener = selectListener
        return this
    }
}