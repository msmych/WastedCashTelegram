package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import wasted.expense.Expense.Category.SHOPPING
import wasted.total.Total

internal class TotalKeypadTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val totalKeypad = TotalKeypad()

    private val total = Total(1, 1000, "USD", SHOPPING)

    @BeforeEach
    fun setUp() {
        totalKeypad.bot = bot
    }

    @Test
    fun sending() {
        totalKeypad.send(1L, listOf(total))
        verify(bot).execute(any<SendMessage>())
    }

    @Test
    fun updating() {
        totalKeypad.update(1L, 2, listOf(total), "month")
        verify(bot).execute(any<EditMessageText>())
    }
}