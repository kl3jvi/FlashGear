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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kl3jvi.yonda.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashGearTopBar(
    modifier: Modifier,
    title: String = stringResource(id = R.string.app_name),
    show: Boolean = true,
    toggleScan: () -> Boolean = { false },
) {
    if (show)
        TopAppBar(
            title = { Text(text = title) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            actions = {
                IconButton(onClick = {
                    !toggleScan()
                }) {
                    Icon(
                        imageVector = if (toggleScan()) Icons.Default.BluetoothDisabled else Icons.Default.Bluetooth,
                        contentDescription = stringResource(id = R.string.app_name),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
}