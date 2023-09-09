package com.kl3jvi.yonda.ext

import com.welie.blessed.ScanFailure

/**
 * `Error` is a data class that holds either a `ScanFailure` or a `Throwable` and provides a method to
 * get the error message.
 * @property {ScanFailure?} scanFailure - This is the error that occurred during the scanning process.
 * @property {Throwable?} throwable - This is the exception that was thrown by the scanner.
 */
data class Error(
    val scanFailure: ScanFailure? = null,
    val throwable: Throwable? = null,
) {
    /**
     * If throwable is not null, return its localized message, otherwise if scanFailure is not null,
     * return its name, otherwise return "Error Occurred"
     *
     * @return The error message
     */
    fun getErrorMessage(): String =
        throwable?.localizedMessage ?: scanFailure?.name ?: "Error Occurred"
}
