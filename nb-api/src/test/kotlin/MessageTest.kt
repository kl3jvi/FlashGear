import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.ScooterMessage
import org.junit.Test
import kotlin.test.assertEquals

class MessageTest {

    @Test
    fun testMessageBuilder() {
        val message = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x7C)
            .setPayload(0x02)
            .build()
        assertEquals(message, "0055aa0320017c025dff")
    }

    @Test
    fun checkCruiseOffString() {
        val message = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.WRITE)
            .setPosition(0x7C)
            .setPayload(0x0000)
            .build()

        assertEquals(message, "0055aa0320037c005dff")
    }

    /* A test to check the lights message string. */
    @Test
    fun checkLightsMessageString() {
        val message = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x7D)
            .setPayload(0x02)
            .build()

        assertEquals(message, "0055aa0320017d025cff")
    }
}
