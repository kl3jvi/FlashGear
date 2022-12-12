package com.kl3jvi.yonda.ext

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.random.Random

val blePermissions = arrayOf(
    BLUETOOTH_CONNECT,
    BLUETOOTH_SCAN
)

fun Fragment.enableBluetooth() {
    if (!isBluetoothGranted()) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            blePermissions,
            Random.nextInt(0, 100)
        )
    }
}

fun Fragment.isBluetoothGranted(): Boolean {
    val bluetoothManager =
        requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    return blePermissions.map {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }.all { it } && bluetoothManager.adapter != null
}

fun Fragment.enableLocation() {
    return
}

fun Fragment.isLocationGranted(vararg permissions: String): Boolean {
    return true
}
