package com.kl3jvi.yonda.ui.screens

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.kl3jvi.yonda.ui.components.BleStatusView

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Manifest.permission.BLUETOOTH_CONNECT
    } else {
        Manifest.permission.BLUETOOTH
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Open camera
        } else {
            // Show dialog
        }
    }

    if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
    } else if (!bluetoothAdapter.isEnabled) {
        // Bluetooth is disabled
        BleStatusView(modifier = modifier) {
            checkAndRequestBluetoothPermission(
                context,
                permission,
                launcher
            ) {
                bluetoothAdapter.enable()
            }
        }
    } else {
        Log.e("TAG", "HomeScreen: ")
        // Bluetooth is enabled
        navController.navigate(Screen.Dashboard.route) {
            // Use this to make sure the navigation command only gets executed once
            launchSingleTop = true
        }
    }

}


fun checkAndRequestBluetoothPermission(
    context: Context,
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    permissionGranted: () -> Unit = {}
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        permissionGranted()
    } else {
        // Request a permission
        launcher.launch(permission)
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}