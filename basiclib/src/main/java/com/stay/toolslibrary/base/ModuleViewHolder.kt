package com.stay.toolslibrary.base

import android.util.SparseArray
import android.view.View
import android.widget.TextView

/**
 * Created by huangyuru on 2016/9/12.
 */
class ModuleViewHolder(val itemView: View) {

    //    集合类，layout里包含的View,以view的id作为key，value是view对象
    private val mViews: SparseArray<View> = SparseArray()//

    fun <T : View> findViewById(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }


    fun setText(viewId: Int, value: String): ModuleViewHolder {
        val view = findViewById<TextView>(viewId)
        view.text = value
        return this
    }

    fun setText(viewId: Int, textId: Int) {
        val textView: TextView = findViewById(viewId)
        textView.setText(textId)
    }

    fun setTextColor(viewId: Int, colorId: Int) {
        val textView: TextView = findViewById(viewId)
        textView.setTextColor(colorId)
    }

    fun setOnClickListener(viewId: Int, clickListener: View.OnClickListener) {
        val view: View = findViewById(viewId)
        view.setOnClickListener(clickListener)
    }


    fun getView(): View {
        return itemView
    }
}
