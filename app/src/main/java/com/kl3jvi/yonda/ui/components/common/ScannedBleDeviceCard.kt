package com.kl3jvi.yonda.ui.components.common

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.CircleShape
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
import com.kl3jvi.yonda.ble.spec.FlashGear

@Composable
fun ScannedBleDeviceCard(
    modifier: Modifier,
    discoveredBluetoothDevice: DemoHolder,
    onClick: (DemoHolder) -> Unit,
) {
    val distanceColor = getDistanceColor(discoveredBluetoothDevice.rssi)

    val connectionState = discoveredBluetoothDevice.connectionState

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
                Spacer(modifier = modifier.fillMaxWidth(0.4f))
                ConnectionStateIndicator(connectionState)

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
                    tint = MaterialTheme.colors.onBackground,
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(text = discoveredBluetoothDevice.address)

                // Add a spacer between address and rssi value
                Spacer(modifier = modifier.fillMaxWidth(0.6f))

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
        rssi >= -70 -> Brush.horizontalGradient(
            listOf(
                Color(0xFF50C750),
                Color(0xFF32B332)
            )
        ) // Excellent signal
        rssi in -85..-71 -> Brush.horizontalGradient(
            listOf(
                Color(0xFFA2C948),
                Color(0xFF8AB032)
            )
        ) // Good signal
        rssi in -100..-86 -> Brush.horizontalGradient(
            listOf(
                Color(0xFFFFB73D),
                Color(0xFFFFA000)
            )
        ) // Fair signal
        else -> Brush.horizontalGradient(
            listOf(
                Color(0xFFFF3D3D),
                Color(0xFFFF0000)
            )
        ) // Poor signal
    }
}

@Composable
fun ConnectionStateIndicator(connectionState: FlashGear.State) {

    val (color, text) = when (connectionState) {
        FlashGear.State.LOADING -> Pair(MaterialTheme.colors.secondaryVariant, "Connecting...")
        FlashGear.State.READY -> Pair(MaterialTheme.colors.onSecondary, "Connected")
        FlashGear.State.NOT_AVAILABLE -> Pair(MaterialTheme.colors.onPrimary, "")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = color)
    }
}

@Preview(showBackground = true)
@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "Full Preview", showSystemUi = true)
@Composable
fun ScannedBleDeviceCardPreview() {
    val demoList = buildList {
        add(
            DemoHolder(
                rssi = -100,
                name = "Test",
                address = "00:00:00:00:00:00",
                connectionState = FlashGear.State.READY,
            )
        )
        add(
            DemoHolder(
                rssi = -10,
                name = "Tests",
                address = "00:00:00:00:00:00",
                connectionState = FlashGear.State.LOADING,
            )
        )
        add(
            DemoHolder(
                rssi = -75,
                name = "Teadsfst",
                address = "00:00:00:00:00:00",
                connectionState = FlashGear.State.NOT_AVAILABLE,
            )
        )
    }

    Column {
        (demoList + demoList)
            .shuffled()
            .forEach { demoHolder ->
                ScannedBleDeviceCard(
                    modifier = Modifier,
                    discoveredBluetoothDevice = demoHolder
                ) {}
                // To add space between cards in the preview
            }
    }
}


data class DemoHolder(
    val rssi: Int,
    val name: String,
    val address: String,
    val connectionState: FlashGear.State,
)

