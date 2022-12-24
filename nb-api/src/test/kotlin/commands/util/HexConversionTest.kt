package commands.util

import com.kl3jvi.nb_api.command.util.bytesToHex
import com.kl3jvi.nb_api.command.util.hexToBytes
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

class HexConversionTest {

    @Test
    fun `test bytesToHex`() {
        val input = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
        val expectedOutput = "0102030405"
        val actualOutput = input.bytesToHex()
        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun `test hexToBytes`() {
        val input = "0102030405"
        val expectedOutput = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)
        val actualOutput = input.hexToBytes()
        assertArrayEquals(expectedOutput, actualOutput)
    }
}
