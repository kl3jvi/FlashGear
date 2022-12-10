package com.kl3jvi.yonda.ext

/**
 * "If the predicate is false, execute the block."
 *
 * The function is marked as inline, which means that the compiler will replace the function call with
 * the function body. This is important because it means that the block will be executed only if the
 * predicate is false
 *
 * @param predicate The condition that must be true for the block to be executed.
 * @param block () -> Unit
 */
public inline fun executeIf(predicate: Boolean, block: () -> Unit) {
    if (predicate) {
        block()
    } else return
}