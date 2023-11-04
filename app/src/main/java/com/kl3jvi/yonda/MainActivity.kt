package com.kl3jvi.yonda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ui.components.FlashGearTopBar
import com.kl3jvi.yonda.ui.components.NavigationGraph
import com.kl3jvi.yonda.ui.screens.HomeViewModel
import com.kl3jvi.yonda.ui.theme.FlashGearTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: HomeViewModel by viewModel()
            val state by viewModel.state.collectAsStateWithLifecycle(ScanningState.Loading)
            var isOnHome by remember { mutableStateOf(false) }
            FlashGearTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column {
                        FlashGearTopBar(
                            navController = navController,
                            isHomeScreen = isOnHome,
                            toggleScan = {
                                viewModel.sendCommand()
                            },
                        )
                        NavigationGraph(
                            scanningState = state,
                            isOnHome = {
                                isOnHome = it
                            },
                            connectDevice = {
                                viewModel.connect(it)
                            },
                        )
                    }
                }
            }
        }
    }
}
