package com.kl3jvi.nb_api.command.util

val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

/**
 * It converts a byte array to a hex string.
 *
 * @param bytes The byte array to convert to hex
 * @return A string of hexadecimal characters.
 */
fun bytesToHex(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (j in bytes.indices) {
        val v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}

/**
 *  It converts a hexadecimal string to a byte array.
 */
fun String.hexToBytes(): ByteArray {
    require(this.length % 2 != 1) { "hexToBytes requires an even-length String parameter" }
    val len = this.length
    val data = ByteArray(len / 2)
    for (i in 0 until len step 2) {
        this[i].digitToIntOrNull(16)?.let { hn ->
            this[i + 1].digitToIntOrNull(16)?.let { ln ->
                data[i / 2] = ((hn shl 4) + ln).toByte()
            }
        }
    }
    return data
}
