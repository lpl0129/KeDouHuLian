package com.stay.toolslibrary.widget.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet

/**
 * Created by Maxim Dybarsky | maxim.dybarskyy@gmail.com
 * on 05.05.15 at 14:45
 */
class AnimatorPlayer(animators: MutableList<Animator>) : AnimatorListenerAdapter() {

    private var interrupted = false

    private var animaorset: AnimatorSet = AnimatorSet()

    init {
        animaorset.playTogether(animators)
        animaorset.addListener(this)
        animaorset.start()
    }

    override fun onAnimationEnd(animation: Animator) {
        if (!interrupted) animate()
    }


    fun stop() {
        interrupted = true
        animaorset.removeListener(this)
        animaorset.cancel()
    }

    private fun animate() {
        animaorset.start()
    }
}
