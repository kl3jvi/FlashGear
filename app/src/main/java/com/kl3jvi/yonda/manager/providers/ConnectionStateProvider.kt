package com.kl3jvi.yonda.manager.providers

import com.kl3jvi.yonda.manager.observers.SuspendConnectionObserver
import no.nordicsemi.android.ble.annotation.ConnectionState

interface ConnectionStateProvider {
    fun isReady(): Boolean

    @ConnectionState
    fun getConnectionState(): Int

    fun subscribeOnConnectionState(observer: SuspendConnectionObserver)

    fun unsubscribeConnectionState(observer: SuspendConnectionObserver)
}
