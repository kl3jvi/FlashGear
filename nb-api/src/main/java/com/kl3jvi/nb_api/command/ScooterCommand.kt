package com.kl3jvi.nb_api.command

/* `ScooterCommand` is an abstract class that defines the properties and methods that all commands must
have */
abstract class ScooterCommand {
    abstract val tag: String
    abstract val name: String
    abstract val requestType: RequestType
    abstract val requestBit: String

    open val defaultUnit: String = ""
    open val handler: (RawResponse) -> String = { it.result }

    abstract fun getRequestString(): String
    fun format(response: ScooterResponse): String = "${response.value}${response.unit}"

    fun handleResponse(deviceRawRawResponse: RawResponse): ScooterResponse {
        return ScooterResponse(
            command = this,
            rawResponse = deviceRawRawResponse,
            value = handler(deviceRawRawResponse),
            unit = defaultUnit
        )
    }
}
