package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.ui.components.common.WarningView
import com.kl3jvi.yonda.ui.theme.FlashGearTheme

@Composable
fun ScanErrorView(error: Int) {
    WarningView(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        imageVector = Icons.Default.BluetoothSearching,
        title = stringResource(id = R.string.scanner_error),
        hint = stringResource(id = R.string.scan_failed, error),
    )
}

@Preview
@Composable
fun ScanErrorViewPreview() {
    FlashGearTheme {
        ScanErrorView(error = 1)
    }
}
