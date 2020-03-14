package com.stay.toolslibrary.widget.dialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.stay.basiclib.R
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog
import com.stay.toolslibrary.utils.extension.dp2px
import kotlinx.android.synthetic.main.base_dialog_loading.*
import java.lang.ref.WeakReference

/**
 * @author huangyr
 * @date 2019/1/30 0030
 */
class LoadingDialog : BaseNiceDialog() {

    private var spots = mutableListOf<AnimatedView>()
    private var spotSize = 5
    private val DELAY = 150
    private val DURATION = 1500
    private val reference = WeakReference<MutableList<AnimatedView>>(spots)
    private var loadingtext = ""
    private var playReference: WeakReference<AnimatorPlayer>? = null

    companion object {
        fun instance(loadingtext: String = "加载中..."): LoadingDialog {
            val bundle = Bundle()
            bundle.putString("text", loadingtext)
            var dialog = LoadingDialog()
            dialog.arguments = bundle
            dialog.setWidth(180)
            return dialog
        }
    }

    override fun intLayoutId(): Int {
        return R.layout.base_dialog_loading
    }

    override fun onStart() {
        super.onStart()
        listener().setListener {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        var text = bundle?.getString("text")
        text?.let {
            loadingtext = text
        }
    }

    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {
        if (TextUtils.isEmpty(loadingtext)) {
            loading_text.visibility = View.GONE
        } else {
            loading_text.visibility = View.VISIBLE
            loading_text.text = loadingtext
        }
        initProgress()
        val aniPlayer = AnimatorPlayer(createAnimations())
        playReference = WeakReference(aniPlayer)
    }

    public fun setLoadingText(LoadingDialog: String) {
        if (loading_text != null) {
            loading_text.text = LoadingDialog
        }

    }

    private fun initProgress() {
        val size = 6.dp2px()
        for (i in 0..spotSize) {
            val v = AnimatedView(context)
            v.setBackgroundResource(R.drawable.toolslib_dialog_spots_bk)
            v.target = 250.dp2px()
            v.xFactor = -1f
            loading_progress.addView(v, size, size)
            reference.get()?.add(v)
        }
    }

    private fun createAnimations(): MutableList<Animator> {
        val animators = mutableListOf<Animator>()
        for (i in 0..spotSize) {
            val move = ObjectAnimator.ofFloat(reference.get()?.get(i), "xFactor", 0.toFloat(), 1.toFloat())
            move.duration = DURATION.toLong()
            move.interpolator = HesitateInterpolator()
            move.startDelay = (DELAY * i).toLong()
            animators.add(move)
        }
        return animators
    }

    override fun onDismiss(dialog: DialogInterface?) {
        playReference?.get()?.stop()
        super.onDismiss(dialog)
    }
}