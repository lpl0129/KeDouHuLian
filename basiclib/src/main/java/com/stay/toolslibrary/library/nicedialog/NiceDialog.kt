package com.stay.toolslibrary.library.nicedialog

import android.os.Bundle
import android.support.annotation.LayoutRes
import com.stay.toolslibrary.base.ModuleViewHolder

class NiceDialog : BaseNiceDialog() {
    private var convertListener: ViewConvertListener? = null

    override fun intLayoutId(): Int {
        return layoutId
    }

    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {
        convertListener?.convertView(holder, dialog)

    }


    fun setLayoutId(@LayoutRes layoutId: Int): NiceDialog {
        super.layoutId = layoutId
        return this
    }

    fun setConvertListener(convertListener: ViewConvertListener): NiceDialog {
        this.convertListener = convertListener
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener")
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("listener", convertListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        convertListener = null
    }

    companion object {

        fun instance(): NiceDialog {
            return NiceDialog()
        }
    }
}
