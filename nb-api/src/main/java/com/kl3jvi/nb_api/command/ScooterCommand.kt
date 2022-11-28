package com.kl3jvi.nb_api.command

abstract class ScooterCommand {
    abstract val tag: String
    abstract val name: String
    abstract val requestType: RequestType
    abstract val requestBit: String
    abstract val defaultUnit: String
    abstract fun getRequestString(): String
    abstract fun handleResponse(deviceRawRawResponse: RawResponse): ScooterResponse
    fun format(response: ScooterResponse): String = "${response.value}${response.unit}"
    open val handler: (RawResponse) -> String = { it.result }
}
