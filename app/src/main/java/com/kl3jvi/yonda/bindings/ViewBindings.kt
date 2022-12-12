package com.kl3jvi.yonda.bindings

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
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
fun ConstraintLayout.bgColor(isScanning: Boolean) {
    val scanColor = ContextCompat.getColor(context, R.color.white)
    val stopColor = ContextCompat.getColor(context, R.color.scanning)
    val arrOfColors = arrayOf(scanColor, stopColor)
    val array = if (isScanning) arrOfColors else arrOfColors.reversedArray()
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), *array)

    colorAnimation.duration = 350L
//    colorAnimation.repeatMode = ValueAnimator.REVERSE
    colorAnimation.setEvaluator(ArgbEvaluator())
    colorAnimation.interpolator = DecelerateInterpolator()
    colorAnimation.addUpdateListener { animation ->
        setBackgroundColor(animation.animatedValue as Int)
    }
    colorAnimation.start()
}

@BindingAdapter("scanningText")
fun TextView.setScanningText(isScanning: Boolean) {
    val anim = AlphaAnimation(1.0f, 0.0f)
    anim.duration = 350L
    anim.repeatCount = 1
    anim.repeatMode = Animation.REVERSE

    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {}
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {
            text =
                if (isScanning) "Scanning For Devices" else "Press to start scanning for devices."
        }
    })
    startAnimation(anim)
}
