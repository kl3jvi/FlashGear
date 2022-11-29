package com.kl3jvi.nb_api.command

data class RawResponse(
    val result: String,
    val elapsedTime: Long
)

data class ScooterResponse(
    val command: ScooterCommand,
    val rawResponse: RawResponse,
    val value: String,
    val unit: String = ""
) {
    val formattedValue: String get() = command.format(this)
}
