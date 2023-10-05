package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.connectivity.FlashGearBluetoothManager
import com.kl3jvi.yonda.connectivity.ConnectionServiceImpl
import com.kl3jvi.yonda.ui.detail.DetailsViewModel
import com.kl3jvi.yonda.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/* Creating a module that will be used to inject dependencies. */
private val bleModule = module {
    single<ConnectionService> { ConnectionServiceImpl(get()) }
    single<FlashGearBluetoothManager> { FlashGearBluetoothManager(get()) }
}

/* Creating a module that will be used to inject dependencies. */
private val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}

val allModules = bleModule + viewModelModule
