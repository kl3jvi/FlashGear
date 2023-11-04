package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.R
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice

@Composable
fun ScannedBleDeviceCard(
    modifier: Modifier,
    discoveredBluetoothDevice: DiscoveredBluetoothDevice,
    onClick: (DiscoveredBluetoothDevice) -> Unit,
) {
    val distanceColor = getDistanceColor(discoveredBluetoothDevice.rssi)
    Card(
        modifier =
        modifier
            .clickable { onClick(discoveredBluetoothDevice) }
            .padding(16.dp, 11.dp, 16.dp, 5.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Row(
                modifier =
                modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                // Scooter icon with green background
                Box(
                    modifier =
                    modifier
                        .size(24.dp)
                        .background(MaterialTheme.colors.onBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.scooter),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = modifier.size(16.dp),
                    )
                }

                Spacer(modifier = modifier.width(8.dp))
                Text(text = discoveredBluetoothDevice.name.orEmpty(), fontWeight = FontWeight.Bold)
            }

            Row(
                modifier =
                modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                // Notebook icon
                Icon(
                    painter = painterResource(id = R.drawable.cpu_bold),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(text = discoveredBluetoothDevice.address)

                // Add a spacer between address and rssi value
                Spacer(modifier = modifier.width(16.dp))

                // Displaying RSSI value
                Text(text = "RSSI: ${discoveredBluetoothDevice.rssi}")
            }
        }

        // Green bottom
        Box(
            modifier =
            modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(distanceColor),
        )
    }
}

@Composable
fun getDistanceColor(rssi: Int): Brush {
    return when {
        rssi > -60 -> Brush.horizontalGradient(listOf(Color(0xFF50C750), Color(0xFF32B332))) // Strong signal
        rssi > -75 -> Brush.horizontalGradient(listOf(Color(0xFFA2C948), Color(0xFF8AB032))) // Good signal
        rssi > -85 -> Brush.horizontalGradient(listOf(Color(0xFFFFB73D), Color(0xFFFFA000))) // Fair signal
        else -> Brush.horizontalGradient(listOf(Color(0xFFFF3D3D), Color(0xFFFF0000)))       // Weak signal
    }
}


@Preview
@Composable
fun ImageCardGridPreview() {
    LazyColumn(content = {
        items(10) {}
    })
}


