package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup

internal class CurrenciesKeypadTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val currenciesKeypad = CurrenciesKeypad()

    @BeforeEach
    fun setUp() {
        currenciesKeypad.bot = bot
    }

    @Test
    fun sending() {
        currenciesKeypad.send(1, ArrayList())
        verify(bot).execute(any<SendMessage>())
    }

    @Test
    fun updating() {
        currenciesKeypad.update(1, 2, ArrayList())
        verify(bot).execute(any<EditMessageReplyMarkup>())
    }
}