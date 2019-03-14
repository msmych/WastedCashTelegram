package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import java.util.*

internal class NumericKeypadTest {

    private val usd = Currency.getInstance("USD")

    private val bot = mock<TelegramLongPollingBot>()

    private val numericKeypad = NumericKeypad()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
    }

    @Test
    fun sending() {
        numericKeypad.send(1, 1000, usd)
        verify(bot).execute(any<SendMessage>())
    }

    @Test
    fun updating() {
        numericKeypad.update(1, 2, 1000, usd)
        verify(bot).execute(any<EditMessageText>())
    }
}