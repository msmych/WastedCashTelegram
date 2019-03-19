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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.expense.ExpenseCategory.SHOPPING
import wasted.rest.RestClient
import java.util.*

internal class OkUpdateProcessorTest {

    private val usd = Currency.getInstance("USD")

    private val expenseCache = mock<ExpenseCache>()
    private val restClient = mock<RestClient>()
    private val bot = mock<TelegramLongPollingBot>()

    private val okUpdateProcessor = OkUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()

    @BeforeEach
    fun setUp() {
        okUpdateProcessor.expenseCache = expenseCache
        okUpdateProcessor.restClient = restClient
        okUpdateProcessor.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("ok")
        whenever(callbackQuery.from).thenReturn(mock())
        whenever(callbackQuery.message).thenReturn(mock())
        whenever(expenseCache.contains(any())).thenReturn(true)
    }

    @Test
    fun applies() {
        assertTrue(okUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notInCacheNotApplies() {
        whenever(expenseCache.contains(any())).thenReturn(false)
        assertFalse(okUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        whenever(expenseCache.remove(any())).thenReturn(ExpenseCacheItem(1, 1000, usd, SHOPPING))
        okUpdateProcessor.process(update)
        verify(expenseCache).remove(any())
        verify(restClient).saveExpense(any(), any(), any(), any(), any(), any())
        verify(bot).execute(any<EditMessageText>())
    }
}