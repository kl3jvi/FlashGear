package com.kl3jvi.yonda.ui.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.ui.components.common.WarningView
import com.kl3jvi.yonda.ui.theme.FlashGearTheme
import no.nordicsemi.android.common.core.parseBold

@Composable
fun ScanEmptyView(
    modifier: Modifier = Modifier,
    requireLocation: Boolean = false,
) {
    WarningView(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        imageVector = Icons.Default.BluetoothSearching,
        title = stringResource(id = R.string.no_device_guide_title),
        hint =
            stringResource(id = R.string.no_device_guide_info) +
                if (requireLocation) {
                    "\n\n" + stringResource(id = R.string.no_device_guide_location_info)
                } else {
                    ""
                }.parseBold(),
        hintTextAlign = TextAlign.Justify,
    ) {
        if (requireLocation) {
            val context = LocalContext.current
            Button(onClick = { openLocationSettings(context) }) {
                Text(text = stringResource(id = R.string.title_scan))
            }
        }
    }
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview
@Composable
private fun ScanEmptyViewPreview_RequiredLocation() {
    FlashGearTheme {
        ScanEmptyView(
            modifier = Modifier.background(MaterialTheme.colorScheme.error),
            requireLocation = true,
        )
    }
}
