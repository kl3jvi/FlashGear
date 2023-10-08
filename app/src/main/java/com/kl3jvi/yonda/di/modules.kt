package com.kl3jvi.yonda.di

import android.bluetooth.BluetoothManager
import android.content.Context
import com.kl3jvi.yonda.manager.FlashGearBluetoothManager
import com.kl3jvi.yonda.manager.scanner.FlashGearScanner
import com.kl3jvi.yonda.manager.scanner.FlashGearScannerImpl
import com.kl3jvi.yonda.manager.service.BluetoothGattServiceWrapper
import com.kl3jvi.yonda.manager.service.FlashGearGattServiceHandler
import com.kl3jvi.yonda.ui.screens.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val bleModule = module {
    single<FlashGearBluetoothManager> { FlashGearBluetoothManager(get(), get(), get()) }
    single<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
    single<BluetoothGattServiceWrapper> { FlashGearGattServiceHandler() }
    single<BluetoothManager> { androidContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    single<FlashGearScanner> { FlashGearScannerImpl(get(), get(), get()) }
}

private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

val allModules = bleModule + viewModelModule
