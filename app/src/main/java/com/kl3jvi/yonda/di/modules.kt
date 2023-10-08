package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.manager.ConnectionService
import com.kl3jvi.yonda.manager.ConnectionServiceImpl
import com.kl3jvi.yonda.manager.FlashGearBluetoothManager
import com.kl3jvi.yonda.manager.service.BluetoothGattServiceWrapper
import com.kl3jvi.yonda.manager.service.FlashGearGattServiceHandler
import com.kl3jvi.yonda.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val bleModule = module {
    single<ConnectionService> { ConnectionServiceImpl(get()) }
    single<FlashGearBluetoothManager> { FlashGearBluetoothManager(get(), get(), get()) }
    single<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
    single<BluetoothGattServiceWrapper> { FlashGearGattServiceHandler() }
}

private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

val allModules = bleModule + viewModelModule
