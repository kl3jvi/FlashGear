package com.kl3jvi.yonda.models

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import com.kl3jvi.yonda.connectivity.ConnectionState
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
    val errorCode: Int? = null,
    val state: ConnectionState= ConnectionState.Disconnected(device!!)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScanHolder

        return device?.address == other.device?.address
    }

    override fun hashCode(): Int {
        return device?.address?.hashCode() ?: 0
    }

    fun getDeviceName(): String {
        return device?.name ?: ""
    }

    fun getRssiString(): String {
        return "RSSI: ${rssi?.toString() ?: "Unknown"}"
    }
}
