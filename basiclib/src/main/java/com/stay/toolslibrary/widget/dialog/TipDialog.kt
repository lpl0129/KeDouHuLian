package com.stay.toolslibrary.widget.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View

import com.stay.basiclib.R
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog
import com.stay.toolslibrary.utils.SpanUtils
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.base_dialog_tip.*

/**
 * @author huangyr
 * @date 2018/10/28
 */
class TipDialog : BaseNiceDialog() {

    private var description = ""
    private var isTip = false
    private var title = ""
    private var rightText = ""
    private var leftText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rightText = it.getString("rightText")!!
            leftText = it.getString("leftText")!!
            description = it.getString("description")!!
            title = it.getString("title")!!
            isTip = it.getBoolean("isTip", false)
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.base_dialog_tip
    }

    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {

        if (title.isNotEmpty()) {
            dialog_title.visibility = View.VISIBLE
            dialog_title.text = title
        }

        if (description.isNotEmpty()) {
            dialog_message.text = description
        } else {
            mListener?.textInitAction?.let {
                val bulder = it()
                dialog_message.text = bulder
            }
        }

        cancle_tv.visibility = if (isTip) View.GONE else View.VISIBLE
        sure_tv.text = rightText
        cancle_tv.text = leftText
        sure_tv.setOnClickListener {
            mListener?.sureClickAction?.let {
                it()
            }
            dismiss()
        }

        cancle_tv.setOnClickListener {
            mListener?.cancleClickAction?.let {
                it()
            }
            dismiss()
        }
    }

    private var mListener: ListenerBuilder? = null

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        mListener?.dismissAction?.let {
            it()
        }

    }
    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit): TipDialog {
        mListener = ListenerBuilder().also(listenerBuilder)
        return this
    }

    inner class ListenerBuilder {
        var sureClickAction: (() -> Unit)? = null

        fun onSureClick(action: () -> Unit) {
            sureClickAction = action
        }

        var cancleClickAction: (() -> Unit)? = null

        var textInitAction: (() -> SpannableStringBuilder)? = null

        fun onTextInit(action: () -> SpannableStringBuilder) {

            textInitAction = action
        }

        fun onCancleClick(action: () -> Unit) {
            cancleClickAction = action
        }
        var dismissAction: (() -> Unit)? = null
        fun onDisMiss(action: () -> Unit) {
            dismissAction = action
        }
    }


    companion object {
        fun newInstance(@NonNull description: String, @NonNull title: String = "", @NonNull isTip: Boolean = false,
                        @NonNull rightText: String = "确认", @NonNull leftText: String = "取消"
        ): TipDialog {
            val bundle = Bundle()
            bundle.putString("description", description)
            bundle.putString("title", title)
            bundle.putBoolean("isTip", isTip)
            bundle.putString("rightText", rightText)
            bundle.putString("leftText", leftText)
            val dialog = TipDialog()
            dialog.setWidth(314)
            dialog.arguments = bundle
            return dialog
        }
    }
}
