package com.kl3jvi.nb_api.command

interface ScooterCommand {
    val tag: String
    val name: String
    val requestType: RequestType
    val requestBit: String
    fun getRequestString(): String
}
