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
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.ExpenseCategory.SHOPPING
import wasted.keypad.NumericKeypad
import java.util.*

internal class WastedClickUpdateProcessorTest {

    private val usd = Currency.getInstance("USD")

    private val expenseCache = mock<ExpenseCache>()
    private val numericKeypad = NumericKeypad()
    private val bot = mock<TelegramLongPollingBot>()

    private val wastedClickUpdateProcessor = WastedClickUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val from = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        wastedClickUpdateProcessor.numericKeypad = numericKeypad
        wastedClickUpdateProcessor.expenseCache = expenseCache
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.from).thenReturn(from)
        whenever(callbackQuery.data).thenReturn("12345")
        whenever(callbackQuery.message).thenReturn(message)
        whenever(expenseCache.contains(any())).thenReturn(true)
        whenever(expenseCache.get(any())).thenReturn(ExpenseCacheItem(1, 1000, usd, SHOPPING))
    }

    @Test
    fun applies() {
        assertTrue(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun over10notApplies() {
        whenever(callbackQuery.data).thenReturn("12345678901234")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun lettersNotApplies() {
        whenever(callbackQuery.data).thenReturn("asdf")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notInCacheNotApplies() {
        whenever(expenseCache.contains(any())).thenReturn(false)
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun noChangeNotApplies() {
        whenever(expenseCache.get(any()))
            .thenReturn(ExpenseCacheItem(1, 0, usd, SHOPPING))
        whenever(callbackQuery.data).thenReturn("0")
        assertFalse(wastedClickUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        whenever(expenseCache.updateAmount(any(), any()))
            .thenReturn(ExpenseCacheItem(1, 1000, usd, SHOPPING))
        wastedClickUpdateProcessor.process(update)
        verify(expenseCache).updateAmount(any(), any())
    }
}