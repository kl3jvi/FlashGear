package com.kl3jvi.yonda.bindings

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.kl3jvi.yonda.R
import com.welie.blessed.ConnectionState


@BindingAdapter("colorIf")
fun ConstraintLayout.setBackgroundColor(connectionState: ConnectionState) {
    background = when (connectionState) {
        ConnectionState.DISCONNECTED -> AppCompatResources.getDrawable(context, R.color.white)
        ConnectionState.CONNECTING -> AppCompatResources.getDrawable(context, R.color.teal_200)
        ConnectionState.CONNECTED -> AppCompatResources.getDrawable(context, R.color.teal_700)
        ConnectionState.DISCONNECTING -> AppCompatResources.getDrawable(context, R.color.white)
    }
}

@BindingAdapter("bgColor")
fun ExtendedFloatingActionButton.bgColor(isScanning: Boolean) {
    val scanColor = ContextCompat.getColor(context, R.color.teal_700)
    val stopColor = ContextCompat.getColor(context, R.color.stop)
    val array = if (isScanning) arrayOf(scanColor, stopColor) else arrayOf(stopColor, scanColor)

    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), *array)

    colorAnimation.duration = 350L
    colorAnimation.setEvaluator(ArgbEvaluator())
    colorAnimation.interpolator = DecelerateInterpolator()
    colorAnimation.addUpdateListener { animation ->
        backgroundTintList = ColorStateList.valueOf(animation.animatedValue as Int)
    }
    colorAnimation.start()

    text = if (isScanning) "Stop" else "Scan"
}


