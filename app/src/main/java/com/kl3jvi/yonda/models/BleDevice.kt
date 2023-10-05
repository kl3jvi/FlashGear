package com.kl3jvi.yonda.models

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import com.welie.blessed.BluetoothPeripheral
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BleDevice(
    val peripheral: @RawValue BluetoothPeripheral,
) : Parcelable

data class ScanHolder(
    val device: BluetoothDevice? = null,
    val rssi: Int? = null,
    val errorCode: Int? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ScanHolder
        return device?.address == other.device?.address
    }

    // Use only address for hashing
    override fun hashCode(): Int {
        return device?.address.hashCode()
    }
}
