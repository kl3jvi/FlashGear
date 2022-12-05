package com.kl3jvi.yonda.ext

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
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

/* Checking if the permissions are granted. */
fun Fragment.isBluetoothGranted(): Boolean {
    return blePermissions.map {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }.all { it }
}

fun Fragment.enableLocation() {
    return
}

fun Fragment.isLocationGranted(vararg permissions: String): Boolean {
    return true
}
