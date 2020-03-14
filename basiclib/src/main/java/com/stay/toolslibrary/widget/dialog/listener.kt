package com.stay.toolslibrary.widget.dialog

/**
 * @author huangyr
 * @date 2019/1/31 0031
 */
class listener {
    private lateinit var mListener: ListenerBuilder

    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var msssAction: (() -> Unit)? = null
        internal var monsuccessAction: (() -> Unit)? = null

        fun onsss(action: () -> Unit) {
            msssAction = action
        }

        fun ononsuccess(action: () -> Unit) {
            monsuccessAction = action
        }
    }

}