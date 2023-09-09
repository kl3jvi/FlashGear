package com.kl3jvi.nb_api.command

/**
 *  Contains all the possible request types that can be sent to the scooter.
 */
enum class RequestType {
    VOLTAGE,
    AMEPERE,
    DISTANCE,
    SPEED,
    SUPER_MASTER,
    SUPER_BATTERY,
    LOCK,
    CRUISE,
    LIGHT,
    RECOVERY,
    BATTERY_LIFE,
    NO_COUNT,
}
