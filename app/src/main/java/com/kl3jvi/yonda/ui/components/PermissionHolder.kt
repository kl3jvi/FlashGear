package com.kl3jvi.yonda.ui.components

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.ui.components.common.FlashGearButtonGradient
import com.kl3jvi.yonda.ui.screens.Screen
import dev.shreyaspatil.permissionflow.compose.rememberMultiplePermissionState
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher

private val locationPermissions =
    arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

private val bluetoothPermissions =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
        )
    }

@Composable
fun PermissionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val permissionLauncher = rememberPermissionFlowRequestLauncher()
    val locationState by rememberMultiplePermissionState(*locationPermissions)
    val blePermission by rememberMultiplePermissionState(*bluetoothPermissions)

    if (locationState.allGranted && blePermission.allGranted) {
        navController.navigate(Screen.Home.route)
    } else {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(Color.White),
        ) {
            Column(
                modifier =
                    modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Default.BluetoothDisabled,
                    contentDescription = null,
                    tint = Color(0xFF589DFF),
                    modifier = modifier.size(64.dp),
                )
                Spacer(modifier = modifier.height(16.dp))
                Text(
                    "Essential Permissions Missing",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Spacer(modifier = modifier.height(16.dp))
                Divider()
                Spacer(modifier = modifier.height(16.dp))
                PermissionItem(
                    icon = Icons.Default.BluetoothDisabled,
                    text = "Bluetooth Permission",
                    enabledState = !blePermission.allGranted,
                    buttonText = if (!blePermission.allGranted) "Allow" else "Allowed",
                ) {
                    Log.d("Permission", "Requesting bluetooth permissions")
                    permissionLauncher.launch(bluetoothPermissions)
                }
                Spacer(modifier = modifier.height(8.dp))
                PermissionItem(
                    icon = Icons.Default.LocationOff,
                    text = "Location Permission",
                    enabledState = !locationState.allGranted,
                    buttonText = if (!locationState.allGranted) "Allow" else "Allowed",
                ) {
                    Log.d("Permission", "Requesting location permissions")
                    permissionLauncher.launch(locationPermissions)
                }
            }
        }
    }
}

@Composable
fun PermissionItem(
    icon: ImageVector,
    text: String,
    buttonText: String,
    enabledState: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF589DFF),
            )
            Text(text, color = Color.Black)
        }
        FlashGearButtonGradient(
            modifier = Modifier.padding(8.dp),
            onClick = { onClick() },
            text = buttonText,
            enabled = enabledState,
            colors = listOf(Color(0xFF50C750), Color(0xFF32B332)),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPermissionsScreen() {
    val navController = rememberNavController()
    PermissionsScreen(navController = navController)
}
