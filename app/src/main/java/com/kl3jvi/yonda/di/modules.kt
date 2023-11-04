package com.kl3jvi.yonda.di

import android.bluetooth.BluetoothManager
import android.content.Context
import com.kl3jvi.yonda.ble.di.scannerModule
import com.kl3jvi.yonda.ui.screens.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

private val bleModule =
    module {
        single<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
        single<BluetoothManager> { androidContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
        single<BluetoothLeScannerCompat> { BluetoothLeScannerCompat.getScanner() }
    }

private val viewModelModule =
    module {
        viewModel { HomeViewModel(get(),get()) }
    }

val allModules = bleModule + viewModelModule + scannerModule
