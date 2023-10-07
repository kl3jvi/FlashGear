package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.manager.ConnectionService
import com.kl3jvi.yonda.manager.ConnectionServiceImpl
import com.kl3jvi.yonda.manager.FlashGearBluetoothManager
import com.kl3jvi.yonda.manager.service.BluetoothGattServiceWrapper
import com.kl3jvi.yonda.manager.service.FlashGearGattServiceHandler
import com.kl3jvi.yonda.ui.detail.DetailsViewModel
import com.kl3jvi.yonda.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/* Creating a module that will be used to inject dependencies. */
private val bleModule = module {
    single<ConnectionService> { ConnectionServiceImpl(get()) }
    single<FlashGearBluetoothManager> { FlashGearBluetoothManager(get(), get(), get()) }
    single<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
    single<BluetoothGattServiceWrapper> { FlashGearGattServiceHandler() }
}

/* Creating a module that will be used to inject dependencies. */
private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}

val allModules = bleModule + viewModelModule
