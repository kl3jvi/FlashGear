package com.kl3jvi.yonda.ext

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

fun Activity.checkSelfPermissions(
    vararg permissions: String
) {
    require(permissions.isNotEmpty()) {
        "Permissions array can not be empty"
    }
    val (permissionForAndroidS, legacyPermissions) =
        permissions.partition { it == BLUETOOTH_CONNECT || it == BLUETOOTH_SCAN }

    val allNotGranted = permissions.map { permission ->
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED
    }.all { it }

    if (allNotGranted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                permissionForAndroidS.toTypedArray(),
                Random.nextInt(0, 100)
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                legacyPermissions.toTypedArray(),
                Random.nextInt(0, 100)
            )
        }
    }
}
