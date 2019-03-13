package wasted.expense

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.NumericKeypad

internal class WastedUpdateProcessorTest {

    private val bot = mock(TelegramLongPollingBot::class.java)
    private val numericKeypad = NumericKeypad()
    private val expenseCache = mock(ExpenseCache::class.java)

    private val expenseUpdateProcessor = WastedUpdateProcessor()

    private val update = mock(Update::class.java)
    private val message = mock(Message::class.java)

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        expenseUpdateProcessor.numericKeypad = numericKeypad
        expenseUpdateProcessor.expenseCache = expenseCache
        `when`(update.message).thenReturn(message)
        `when`(message.from).thenReturn(mock(User::class.java))
    }

    @Test
    fun applies() {
        `when`(message.text).thenReturn("/wasted")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        `when`(message.text).thenReturn("/wrong")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        expenseUpdateProcessor.process(update)
        verify(expenseCache).put(anyInt(), anyLong(), anyString(), anyString())
    }
}