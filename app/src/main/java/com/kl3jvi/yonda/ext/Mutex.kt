package com.kl3jvi.yonda.ext

import android.util.Log
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

suspend inline fun withLock(
    mutex: Mutex,
    tag: String,
    block: () -> Unit,
) {
    if (mutex.isLocked) {
        Log.i(tag, "I can't execute right now job $tag because $mutex is locked")
    }
    mutex.withLock {
        block()
    }
}
