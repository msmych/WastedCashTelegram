package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import wasted.expense.Expense.Category.SHOPPING
import wasted.total.Total

internal class TotalKeypadTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val totalKeypad = TotalKeypad()

    @BeforeEach
    fun setUp() {
        totalKeypad.bot = bot
    }

    @Test
    fun sending() {
        totalKeypad.send(1L, listOf(Total(1, 1000, "USD", SHOPPING)))
        verify(bot).execute(any<SendMessage>())
    }
}