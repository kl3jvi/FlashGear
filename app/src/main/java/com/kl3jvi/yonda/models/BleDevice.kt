package com.kl3jvi.yonda.models

import android.os.Parcelable
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class BleDevice(
    val peripheral: @RawValue BluetoothPeripheral,
    var connectionState: ConnectionState = ConnectionState.DISCONNECTED
) : Parcelable