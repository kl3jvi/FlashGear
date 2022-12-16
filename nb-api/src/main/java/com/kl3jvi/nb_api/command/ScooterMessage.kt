package com.kl3jvi.nb_api.command

class ScooterMessage {
    private var msg: MutableList<Int> = mutableListOf()
    private var direction = 0
    private var rw = 0
    private var position = 0
    private var payload: MutableList<Int> = mutableListOf()
    private var checksum = 0

    fun setDirection(drct: Commands): ScooterMessage {
        direction = drct.command
        checksum += direction
        return this
    }

    fun setReadOrWrite(readOrWrite: Commands): ScooterMessage { // read or write
        rw = readOrWrite.command
        checksum += rw
        return this
    }

    fun setPosition(pos: Int): ScooterMessage {
        position = pos
        checksum += position
        return this
    }

    fun setPayload(bytesToSend: ByteArray): ScooterMessage {
        payload = mutableListOf()
        checksum += bytesToSend.size + 2
        for (b in bytesToSend) {
            payload.add(b.toInt())
            checksum += b
        }
        return this
    }

    fun setPayload(bytesToSend: MutableList<Int>): ScooterMessage {
        payload = bytesToSend
        checksum += payload.size + 2
        for (i in payload) {
            checksum += i
        }
        return this
    }

    fun setPayload(singleByteToSend: Int): ScooterMessage {
        payload = mutableListOf()
        payload.add(singleByteToSend)
        checksum += 3
        checksum += singleByteToSend
        return this
    }

    fun build(): String {
        setupHeaders()
        setupBody()
        calculateChecksum()
        return construct()
    }

    private fun setupHeaders() {
        msg = mutableListOf()
        msg.add(0x55)
        msg.add(0xAA)
    }

    private fun setupBody() {
        msg.add(payload.size + 2)
        msg.add(direction)
        msg.add(rw)
        msg.add(position)
        for (i in payload) {
            msg.add(i)
        }
    }

    private fun calculateChecksum() {
        checksum = checksum xor 0xffff
        msg.add(checksum and 0xff)
        msg.add(checksum shr 8)
    }

    /**
     * It converts a byte array into a hexadecimal string.
     *
     * @return The message is being returned as a string of hexadecimal values.
     */
    private fun construct(): String {
        return msg.joinToString("") { it.toString(16).padStart(2, '0') }
    }
}
