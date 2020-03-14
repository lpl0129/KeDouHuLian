package com.stay.toolslibrary.widget.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.stay.basiclib.R
import com.stay.toolslibrary.base.ModuleAdpaer
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog
import com.stay.toolslibrary.utils.extension.toArrayList

/**
 * @author huangyr
 * @date 2019/4/4 0004
 */
class ListDialog : BaseNiceDialog() {

    private var list: MutableList<String>? = null
    private var selestText = ""

    companion object {
        fun newInstance(list: MutableList<String>, selestText: String = ""): ListDialog {

            val bundle = Bundle()
            bundle.putStringArrayList("list", list.toArrayList())
            bundle.putString("selestText", selestText)
            val dialog = ListDialog()
            dialog.setWidth(314)
            dialog.setShowBottom(true)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getStringArrayList("list")
            selestText = it.getString("selestText")!!
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {

        if (list == null) {
            list = ArrayList()
            list!!.add("男")
            list!!.add("女")
        }
        context?.let {
            holder.findViewById<ListView>(R.id.listview).adapter = DialogAdapter(it, list!!, selestText)
        }
        holder.findViewById<View>(R.id.gl_choose_cancel).setOnClickListener {
            dismiss()
        }
    }

    private var mListener: ListenerBuilder? = null

    fun setListener(listenerBuilder: ListDialog.ListenerBuilder.() -> Unit): ListDialog {
        mListener = ListenerBuilder().also(listenerBuilder)
        return this
    }


    override fun intLayoutId(): Int = R.layout.base_dialog_list_chose

    inner class DialogAdapter(context: Context, result: List<String>, private val selectTexttext: String) :
        ModuleAdpaer<String>(context, result) {


        override fun getLayoutIdType(type: Int): Int {
            return R.layout.dialog_list_item
        }

        override fun bindData(viewHolder: ModuleViewHolder, bean: String, position: Int) {

            val text = viewHolder.findViewById<TextView>(R.id.text_bt)
            text.text = bean
            text.setOnClickListener {
                mListener?.onListItemClick?.let {
                    it(position, bean)
                }
                dismiss()
            }
            if (bean == selectTexttext) {
                text.setTextColor(context.getResources().getColor(R.color.primaryColor))
            } else {
                text.setTextColor(context.getResources().getColor(R.color.deepColor))
            }
        }
    }

    inner class ListenerBuilder {
        var onListItemClick: ((position: Int, selectText: String) -> Unit)? = null
        fun setListItemClick(action: (position: Int, selectText: String) -> Unit) {
            onListItemClick = action
        }
    }
}