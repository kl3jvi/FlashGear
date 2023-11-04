package com.kl3jvi.yonda.ble.di

import com.kl3jvi.yonda.ble.FlashGearManager
import com.kl3jvi.yonda.ble.repository.FlashGearRepository
import com.kl3jvi.yonda.ble.scanner.FlashGearScanner
import com.kl3jvi.yonda.ble.scanner.FlashGearScannerImpl
import com.kl3jvi.yonda.ble.spec.FlashGear
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repoModule = module {
    single<FlashGearScanner> { FlashGearScannerImpl() }
}

val scannerModule = module {
    single<FlashGear> { FlashGearManager(androidApplication().applicationContext) }
    single { FlashGearRepository(get()) }
} + repoModule

