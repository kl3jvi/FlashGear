package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.core.parseBold

@Composable
fun ScanErrorView(
    error: Int,
) {
    WarningView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        imageVector = Icons.Default.BluetoothSearching,
        title = "stringResource(id = R.string.scanner_error)",
        hint = "stringResource(id = R.string.scan_failed, error)".parseBold()
    )
}