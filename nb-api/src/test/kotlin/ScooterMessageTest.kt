import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.ScooterMessage
import org.junit.Test
import kotlin.test.assertEquals


class ScooterMessageTest {
    private val scooterMessage = ScooterMessage()

    @Test
    fun testSetDirection() {
        val expectedMessage = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
        assertEquals(
            expectedMessage.direction,
            scooterMessage.setDirection(Commands.MASTER_TO_M365).direction
        )
    }

    @Test
    fun testSetReadOrWrite() {
        val expectedMessage = ScooterMessage()
            .setReadOrWrite(Commands.WRITE)
        assertEquals(expectedMessage.rw, scooterMessage.setReadOrWrite(Commands.WRITE).rw)
    }

    @Test
    fun testSetPosition() {
        val expectedMessage = ScooterMessage()
            .setPosition(0x7C)
        assertEquals(expectedMessage.position, scooterMessage.setPosition(0x7C).position)
    }

    @Test
    fun testSetPayload_byteArray() {
        val bytesToSend = byteArrayOf(0x01, 0x02, 0x03)
        val expectedMessage = ScooterMessage()
            .setPayload(bytesToSend)
        assertEquals(expectedMessage.payload, scooterMessage.setPayload(bytesToSend).payload)
    }

    @Test
    fun testSetPayload_list() {
        val bytesToSend = mutableListOf(0x01, 0x02, 0x03)
        val expectedMessage = ScooterMessage()
            .setPayload(bytesToSend)
        assertEquals(expectedMessage.payload, scooterMessage.setPayload(bytesToSend).payload)
    }

    @Test
    fun testSetPayload_singleByte() {
        val singleByteToSend = 0x01
        val expectedMessage = ScooterMessage()
            .setPayload(singleByteToSend)
        assertEquals(expectedMessage.payload, scooterMessage.setPayload(singleByteToSend).payload)
    }

    @Test
    fun testBuild() {
        val expectedMessage = "55aa0320037c015cff"
        assertEquals(
            expectedMessage, scooterMessage
                .setDirection(Commands.MASTER_TO_M365)
                .setReadOrWrite(Commands.WRITE)
                .setPosition(0x7C)
                .setPayload(0x01)
                .build()
        )
    }
}
