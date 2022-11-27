package com.kl3jvi.nb_api.command

/* An enum class that contains the commands that are used to communicate with the scooter. */
enum class Commands(val command: Int) {
    MASTER_TO_M365(0x20),
    MASTER_TO_BATTERY(0x22),
    READ(0x01),
    WRITE(0x03);
}
