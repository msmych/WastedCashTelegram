package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.NumericKeypad
import java.time.Instant.now

internal class WastedUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val numericKeypad = NumericKeypad()
    private val expenseClient = mock<ExpenseClient>()

    private val expenseUpdateProcessor = WastedUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        expenseUpdateProcessor.numericKeypad = numericKeypad
        expenseUpdateProcessor.expenseClient = expenseClient
        expenseUpdateProcessor.bot = bot
        whenever(update.message).thenReturn(message)
        whenever(message.from).thenReturn(mock())
    }

    @Test
    fun applies() {
        whenever(message.text).thenReturn("/wasted")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        whenever(message.text).thenReturn("/wrong")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        whenever(expenseClient.createExpense(any()))
            .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, now()))
        whenever(bot.execute(any<SendMessage>())).thenReturn(message)
        expenseUpdateProcessor.process(update)
        verify(expenseClient).createExpense(any())
    }
}
