package com.kl3jvi.nb_api.command

class Message {
    private var msg: MutableList<Int> = mutableListOf()
    private var direction = 0
    private var rw = 0
    private var position = 0
    private var payload: MutableList<Int> = mutableListOf()
    private var checksum = 0

    fun setDirection(drct: Commands): Message {
        direction = drct.command
        checksum += direction
        return this
    }

    fun setRW(readOrWrite: Commands): Message { // read or write
        rw = readOrWrite.command
        checksum += rw
        return this
    }

    fun setPosition(pos: Int): Message {
        position = pos
        checksum += position
        return this
    }

    fun setPayload(bytesToSend: ByteArray): Message {
        payload = mutableListOf<Int>()
        checksum += bytesToSend.size + 2
        for (b in bytesToSend) {
            payload.add(b.toInt())
            checksum += b
        }
        return this
    }

    fun setPayload(bytesToSend: MutableList<Int>): Message {
        payload = bytesToSend
        checksum += payload.size + 2
        for (i in payload) {
            checksum += i
        }
        return this
    }

    fun setPayload(singleByteToSend: Int): Message {
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
        msg = mutableListOf(0)
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

    private fun construct(): String {
        return msg.joinToString("") { it.toString(16).padStart(2, '0') }
    }
}