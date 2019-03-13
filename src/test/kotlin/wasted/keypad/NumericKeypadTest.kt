package wasted.keypad

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import java.util.*

internal class NumericKeypadTest {

    private val bot = Mockito.mock(TelegramLongPollingBot::class.java)

    private val numericKeypad = NumericKeypad()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
    }

    @Test
    fun sending() {
        numericKeypad.send(1, 1000, Currency.getInstance("USD"))
        verify(bot).execute(isA(SendMessage::class.java))
    }

    @Test
    fun updating() {
        numericKeypad.update(1, 2, 1000, Currency.getInstance("USD"))
        verify(bot).execute(isA(EditMessageText::class.java))
    }
}