package com.kl3jvi.yonda.bindings

import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
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