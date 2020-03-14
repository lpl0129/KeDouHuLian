package com.stay.toolslibrary.base

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import kotlinx.android.extensions.LayoutContainer

/**
 * @author huangyr
 * @date 2019/2/14 0014
 */
class RecyclerViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private val mViews: SparseArray<View> = SparseArray()

    fun <T : View> findViewById(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun setText(@IdRes viewId: Int, value: String): RecyclerViewHolder {
        val view = findViewById<TextView>(viewId)
        view.text = value
        return this
    }

    fun setText(@IdRes viewId: Int, textId: Int) {
        val textView: TextView = findViewById(viewId)
        textView.setText(textId)
    }

    fun setTextColor(@IdRes viewId: Int, colorId: Int) {
        val textView: TextView = findViewById(viewId)
        textView.setTextColor(colorId)
    }

    fun setClickListener(@IdRes vararg viewIds: Int, listener: (View) -> Unit) {
        viewIds.forEach { id ->
            val view: View = findViewById(id)
            view.setOnClickListener {
                listener(it)
            }
        }
    }

    fun setOnLongClickListener(@IdRes vararg viewIds: Int, listener: (View) -> Unit) {
        viewIds.forEach { id ->
            val view: View = findViewById(id)
            view.setOnLongClickListener {
                listener(it)
                true
            }
        }
    }
}
