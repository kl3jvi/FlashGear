package com.kl3jvi.nb_api.command

class Message {
    data class Builder(
        private var msg: List<Int> = emptyList(),
        private var direction: Int = 0,
        private var rw: Int = 0,
        private var position: Int = 0,
        private var payload: List<Int> = mutableListOf(),
        private var checksum: Int = 0
    ) {

        fun setDirection(command: Commands) = apply {
            this.direction = command.command
            this.checksum = checksum.plus(this.direction)
        }

        /**
         * It sets the read/write bit of the command.
         *
         * @param readOrWrite Commands.READ or Commands.WRITE
         */
        fun setRW(readOrWrite: Commands) = apply {
            this.rw = readOrWrite.command
            this.checksum = checksum.plus(rw)
        }

        /**
         * Set the position and update the checksum.
         *
         * @param pos The position of the character in the string.
         */
        fun setPosition(pos: Int) = apply {
            position = pos
            checksum = checksum.plus(position)
        }

        /**
         * It takes an array of bytes and adds them to the payload of a packet.
         *
         * @param bytesToSend The payload to be sent.
         */
        fun setPayload(bytesToSend: ArrayList<Byte>) = apply {
            checksum += bytesToSend.size + 2

            payload = buildList {
                bytesToSend.forEach { byte ->
                    add(byte.toInt())
                    checksum = checksum.plus(byte)
                }
            }
        }

        /**
         * `setPayload` takes a list of integers and adds them to the payload of the packet
         *
         * @param bytesToSend The payload to be sent.
         */
        fun setPayload(bytesToSend: List<Int>) = apply {
            payload = bytesToSend
            checksum += payload.size + 2

            for (i in payload) {
                checksum += i
            }
        }

        fun setPayload(singleByteToSend: Int) = apply {
            payload = buildList { add(singleByteToSend) }
            checksum += 3
            checksum += singleByteToSend
        }

        private fun setupHeader() = apply {
            msg = buildList {
                add(0x55)
                add(0xAA)
            }
        }

        private fun setupBody() = apply {
            msg = buildList {
                add(payload.size + 2)
                add(direction)
                add(rw)
                add(position)
                addAll(payload)
            }
        }

        private fun calculateChecksum() = apply {
            checksum = checksum xor 0xffff
            msg = buildList {
                add(checksum and 0xff)
                add(checksum shr 8)
            }
        }

        /* Converting the list of integers into a string. */
        private fun construct(): String = msg.joinToString("") {
            if (it in 0..15) {
                "0" + Integer.toHexString(it)
            } else {
                Integer.toHexString(it)
            }
        }

        fun build(): String {
            setupHeader()
            setupBody()
            calculateChecksum()

            return construct()
        }
    }
}
