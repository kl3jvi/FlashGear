package com.kl3jvi.yonda.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.kl3jvi.yonda.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashGearTopBar(
    navController: NavHostController,
    isHomeScreen: Boolean,
    title: String = stringResource(id = R.string.app_name),
    toggleScan: (Boolean) -> Unit,
) {
    var scanBool by remember { mutableStateOf(true) }

    TopAppBar(
        title = { Text(text = title) },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        actions = {
            if (isHomeScreen) {
                IconButton(onClick = {
                    scanBool = !scanBool
                    toggleScan(scanBool)
                }) {
                    Icon(
                        imageVector = if (scanBool) Icons.Default.BluetoothDisabled else Icons.Default.Bluetooth,
                        contentDescription = stringResource(id = R.string.app_name),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
    )
}
