package com.kl3jvi.yonda.ext

import com.welie.blessed.ScanFailure

abstract class YondaException : RuntimeException() {
    companion object {
        fun checkForExceptions(scanFailure: ScanFailure) {
            when (scanFailure) {
                ScanFailure.ALREADY_STARTED -> throw ScanAlreadyStartedError()
                ScanFailure.APPLICATION_REGISTRATION_FAILED -> throw ApplicationRegistrationFailed()
                ScanFailure.INTERNAL_ERROR -> throw InternalError()
                ScanFailure.FEATURE_UNSUPPORTED -> throw FeatureUnsupportedError()
                ScanFailure.OUT_OF_HARDWARE_RESOURCES -> TODO()
                ScanFailure.SCANNING_TOO_FREQUENTLY -> TODO()
                ScanFailure.UNKNOWN -> TODO()
                else -> throw GeneralError()
            }
        }
    }
}

private typealias ERROR_TYPE = YondaException

class GeneralError : ERROR_TYPE()
class ScanAlreadyStartedError : ERROR_TYPE()
class ApplicationRegistrationFailed : ERROR_TYPE()
class InternalError : ERROR_TYPE()
class FeatureUnsupportedError : ERROR_TYPE()
