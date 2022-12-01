package com.kl3jvi.yonda.ext

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToastIf(vararg message: String, predicate: () -> Boolean) {
    check(message.size == 2) {
        "Please set 2 strings to use this method!, First shows if predicate is true, second shows if predicate is false."
    }
    if (predicate()) {
        showToast(message.first())
    } else showToast(message.last())
}