package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.ui.screens.home.DevicesScanFilter
import com.kl3jvi.yonda.ui.theme.FlashGearTheme
import no.nordicsemi.android.common.core.parseBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterView(
    config: DevicesScanFilter,
    onChanged: (DevicesScanFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(
            text = "Filter by:".parseBold(),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(start = 16.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier,
        ) {
            config.filterNearbyOnly.let {
                ElevatedFilterChip(
                    selected = it,
                    onClick = { onChanged(config.copy(filterNearbyOnly = !it)) },
                    label = { Text(text = "Nearby") },
                    modifier = Modifier.padding(end = 8.dp),
                    leadingIcon = {
                        if (it) {
                            Icon(Icons.Default.Done, contentDescription = "")
                        } else {
                            Icon(Icons.Default.Wifi, contentDescription = "")
                        }
                    },
                )
            }
            config.filterWithNames.let {
                ElevatedFilterChip(
                    selected = it,
                    onClick = { onChanged(config.copy(filterWithNames = !it)) },
                    label = { Text(text = "Name") },
                    modifier = Modifier.padding(end = 8.dp),
                    leadingIcon = {
                        if (it) {
                            Icon(Icons.Default.Done, contentDescription = "")
                        } else {
                            Icon(Icons.Default.Label, contentDescription = "")
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun FilterViewPreview() {
    FlashGearTheme {
        FilterView(
            config =
                DevicesScanFilter(
                    filterNearbyOnly = false,
                    filterWithNames = true,
                ),
            onChanged = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp),
        )
    }
}
