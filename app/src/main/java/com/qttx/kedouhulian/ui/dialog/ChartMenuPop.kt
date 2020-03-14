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
class ChartMenuPop constructor(mContext: Context) : AttachPopupView(mContext) {

    private var selectListener: OnSelectListener? = null

    override fun getImplLayoutId(): Int {
        return R.layout.chat_pop_menu

    }
    override fun onCreate() {
        super.onCreate()
        findViewById<View>(R.id.addUserLl).setOnClickListener {
            selectListener?.onSelect(0, "")
            if (popupInfo.autoDismiss) dismiss()
        }
        findViewById<View>(R.id.createPrivateLl).setOnClickListener {
            selectListener?.onSelect(1, "")
            if (popupInfo.autoDismiss) dismiss()
        }

        findViewById<View>(R.id.createPublichLl).setOnClickListener {
            selectListener?.onSelect(2, "")
            if (popupInfo.autoDismiss) dismiss()
        }
        findViewById<View>(R.id.myContactLl).setOnClickListener {
            selectListener?.onSelect(3, "")
            if (popupInfo.autoDismiss) dismiss()
        }
        findViewById<View>(R.id.settingLl).setOnClickListener {
            selectListener?.onSelect(4, "")
            if (popupInfo.autoDismiss) dismiss()
        }
    }

    fun setOffsetXAndY(offsetX: Int, offsetY: Int): ChartMenuPop {
        this.defaultOffsetX += offsetX
        this.defaultOffsetY += offsetY
        return this
    }

    fun setOnSelectListener(selectListener: OnSelectListener): ChartMenuPop {
        this.selectListener = selectListener
        return this
    }


}