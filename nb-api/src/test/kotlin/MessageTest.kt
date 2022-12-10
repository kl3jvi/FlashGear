import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.Message
import org.junit.Test
import kotlin.test.assertEquals

class MessageTest {

    @Test
    fun testMessageBuilder() {
        val message = Message.Builder()
            .setDirection(Commands.MASTER_TO_M365)
            .setRW(Commands.READ)
            .setPosition(0x7C)
            .setPayload(0x02)
            .build()

        assertEquals(message, "5dff")
    }

    @Test
    fun checkCruiseOffString() {
        val message = Message.Builder()
            .setDirection(Commands.MASTER_TO_M365)
            .setRW(Commands.WRITE)
            .setPosition(0x7C)
            .setPayload(0x0000)
            .build()
        assertEquals(message, "5dff")
    }

    @Test
    fun checkLightsMessageString() {
        val message = Message.Builder()
            .setDirection(Commands.MASTER_TO_M365)
            .setRW(Commands.READ)
            .setPosition(0x7D)
            .setPayload(0x02)
            .build()


        assertEquals(message, "5cff")
    }


}