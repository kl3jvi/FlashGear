package com.kl3jvi.nb_api.command.ampere

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterMessage

class Ampere : ScooterCommand() {
    override val tag: String = "Ampere"
    override val name: String = "Current current"
    override val requestType: RequestType = RequestType.AMEPERE
    override val requestBit: String = "33"
    override val defaultUnit: String = "A"

    override val handler = { it: RawResponse -> "%.1f".format(getCurrentAmpere(it.result)) }

    override fun getRequestString(): String = ScooterMessage()
        .setDirection(Commands.MASTER_TO_BATTERY)
        .setReadOrWrite(Commands.READ)
        .setPosition(0x33)
        .setPayload(0x02)
        .build()

    /**
     * It takes the result from ble response and returns a double that tells the current ampere on
     * scooter device.
     *
     * @param result The result of the command sent to the device.
     * @return The current ampere value is being returned.
     */
    fun getCurrentAmpere(result: String): Double {
        val temp = result.substring(6..7)
        val amps: Int = Integer.parseInt(temp, 16)
        return amps.toDouble() / 100
    }
}
