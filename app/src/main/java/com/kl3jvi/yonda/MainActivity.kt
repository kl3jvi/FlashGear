package com.kl3jvi.yonda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.ui.screens.HomeScreen
import com.kl3jvi.yonda.ui.screens.HomeViewModel
import com.kl3jvi.yonda.ui.theme.FlashGearTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: HomeViewModel by viewModel()
            val scooterData = viewModel.state.collectAsStateWithLifecycle()
            var isScanning by remember { mutableStateOf(false) }
            FlashGearTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            actions = {
                                IconButton(onClick = {
                                    viewModel.toggleScan(isScanning)
                                    isScanning = !isScanning
                                }) {
                                    Icon(
                                        imageVector = if (isScanning) Icons.Default.BluetoothDisabled else Icons.Default.Bluetooth,
                                        contentDescription = stringResource(id = R.string.app_name),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                        HomeScreen(
                            navController = navController,
                            modifier = Modifier,
                            scooterData.value,
                            {
                                viewModel.connect(it)
                            }, {
                                viewModel.startScan()
                            })
                    }
                }
            }
        }
    }
}
