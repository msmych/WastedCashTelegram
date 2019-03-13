package wasted.expense

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.NumericKeypad

internal class WastedClickUpdateProcessorTest {

    private val expenseCache = mock(ExpenseCache::class.java)
    private val numericKeypad = NumericKeypad()
    private val bot = mock(TelegramLongPollingBot::class.java)

    private val wastedClickUpdateProcessor = WastedClickUpdateProcessor()

    private val update = mock(Update::class.java)
    private val callbackQuery = mock(CallbackQuery::class.java)
    private val from = mock(User::class.java)
    private val message = mock(Message::class.java)

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        wastedClickUpdateProcessor.numericKeypad = numericKeypad
        wastedClickUpdateProcessor.expenseCache = expenseCache
        `when`(update.callbackQuery).thenReturn(callbackQuery)
        `when`(callbackQuery.from).thenReturn(from)
        `when`(callbackQuery.data).thenReturn("12345")
        `when`(callbackQuery.message).thenReturn(message)
        `when`(expenseCache.contains(anyInt())).thenReturn(true)
        `when`(expenseCache.get(anyInt())).thenReturn(ExpenseCacheItem(1, 1000, "USD", "FOOD"))
    }

    @Test
    fun applies() {
        assertTrue(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun over10notApplies() {
        `when`(callbackQuery.data).thenReturn("12345678901234")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun lettersNotApplies() {
        `when`(callbackQuery.data).thenReturn("asdf")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notInCacheNotApplies() {
        `when`(expenseCache.contains(anyInt())).thenReturn(false)
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun noChangeNotApplies() {
        `when`(expenseCache.get(anyInt()))
            .thenReturn(ExpenseCacheItem(1, 0, "USD", "FOOD"))
        `when`(callbackQuery.data).thenReturn("0")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        `when`(expenseCache.updateAmount(anyInt(), anyLong()))
            .thenReturn(ExpenseCacheItem(1, 1000, "USD", "FOOD"))
        wastedClickUpdateProcessor.process(update)
        verify(expenseCache).updateAmount(anyInt(), anyLong())
    }
}