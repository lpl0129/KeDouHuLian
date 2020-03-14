package com.stay.toolslibrary.library.nicedialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.stay.basiclib.R
import com.stay.toolslibrary.base.BaseApplication
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.utils.extension.dp2px
import com.stay.toolslibrary.utils.extension.screenWidth
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment
import java.util.ArrayList

abstract class BaseNiceDialog : RxAppCompatDialogFragment() {

    private var margin: Int = 0//左右边距
    private var width: Int = 0//宽度
    private var height: Int = 0//高度
    private var dimAmount = 0.5f//灰度深浅
    private var showBottom: Boolean = false//是否底部显示
    private var outCancel = true//是否点击外部取消

    // window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
    //设置点击事件可以透传到下方view
    private var widowFlg = ArrayList<Int>()
    @StyleRes
    private var animStyle: Int = 0
    @LayoutRes
    protected var layoutId: Int = 0

    abstract fun intLayoutId(): Int

    abstract fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog)
    private var needAni = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.NiceDialog)
        layoutId = intLayoutId()

        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            dimAmount = savedInstanceState.getFloat(DIM)
            showBottom = savedInstanceState.getBoolean(BOTTOM)
            outCancel = savedInstanceState.getBoolean(CANCEL)
            widowFlg = savedInstanceState.getIntegerArrayList(WINDOWFLG)!!
            animStyle = savedInstanceState.getInt(ANIM)
            layoutId = savedInstanceState.getInt(LAYOUT)
            needAni = savedInstanceState.getBoolean(NEEDANI)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        convertView(ModuleViewHolder(view), this)
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        initParams()
    }

    fun setNeedAni(needAni: Boolean): BaseNiceDialog {
        this.needAni = needAni
        return this
    }
    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MARGIN, margin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putBoolean(BOTTOM, showBottom)
        outState.putBoolean(CANCEL, outCancel)
        outState.putIntegerArrayList(WINDOWFLG, widowFlg)
        outState.putInt(ANIM, animStyle)
        outState.putInt(LAYOUT, layoutId)
        outState.putBoolean(NEEDANI, needAni)
    }

    @SuppressLint("ResourceType")
    private fun initParams() {
        val window = dialog.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation
                }
            }

            //设置dialog宽度
            if (width == 0) {
                lp.width = BaseApplication.getApplication().screenWidth - 2 * margin.dp2px()
            } else if (width == -1) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.width = width.dp2px()
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.height = height.dp2px()
            }
            if (animStyle < 0) {
                animStyle = 0
            }
            if (!widowFlg.isEmpty()) {
                for (item in widowFlg) {
                    window.addFlags(item)
                }
            }
            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle)
            window.attributes = lp
        }
        isCancelable = outCancel
    }

    fun setMargin(margin: Int): BaseNiceDialog {
        this.margin = margin
        return this
    }

    fun setWidth(width: Int): BaseNiceDialog {
        this.width = width
        return this
    }

    fun setWinflg(flg: Int): BaseNiceDialog {
        widowFlg.add(flg)
        return this
    }

    fun setHeight(height: Int): BaseNiceDialog {
        this.height = height
        return this
    }


    fun setDimAmount(dimAmount: Float): BaseNiceDialog {
        this.dimAmount = dimAmount
        return this
    }

    fun setShowBottom(showBottom: Boolean): BaseNiceDialog {
        this.showBottom = showBottom
        return this
    }

    fun getShowBottom(): Boolean {
        return showBottom
    }

    fun setOutCancel(outCancel: Boolean): BaseNiceDialog {
        this.outCancel = outCancel
        return this
    }

    fun setAnimStyle(@StyleRes animStyle: Int): BaseNiceDialog {
        this.animStyle = animStyle
        return this
    }

    fun show(manager: FragmentManager): BaseNiceDialog {
        val ft = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this).commit()
        }
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()
        return this
    }

    companion object {
        private val MARGIN = "margin"
        private val WIDTH = "width"
        private val HEIGHT = "height"
        private val DIM = "dim_amount"
        private val BOTTOM = "show_bottom"
        private val CANCEL = "out_cancel"
        private val WINDOWFLG = "windowflg"
        private val ANIM = "anim_style"
        private val LAYOUT = "layout_id"
        private val NEEDANI = "needAni"
    }
}
