package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import wasted.expense.Expense
import wasted.expense.Expense.Category.SHOPPING
import java.time.Instant.now

internal class NumericKeypadTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val numericKeypad = NumericKeypad()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
    }

    @Test
    fun updating() {
        numericKeypad.update(
            Expense(1, 1, 1, 2, 1000, "USD", SHOPPING, now()))
        verify(bot).execute(any<EditMessageText>())
    }
}
