package wasted.expense.currency

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
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.ExpenseCache
import wasted.expense.ExpenseCacheItem
import wasted.expense.ExpenseCategory.SHOPPING
import wasted.keypad.NumericKeypad
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

internal class NextCurrencyUpdateProcessorTest {

    private val currencies = Stream.of("USD", "EUR", "RUB").map{ Currency.getInstance(it) }.toList()

    private val bot = mock<TelegramLongPollingBot>()
    private val userCurrenciesService = mock<UserCurrenciesService>()
    private val expenseCache = mock<ExpenseCache>()
    private val numericKeypad = NumericKeypad()

    private val nextCurrencyUpdateProcessor = NextCurrencyUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()
    private val from = mock<User>()

    @BeforeEach
    fun setUp() {
        nextCurrencyUpdateProcessor.userCurrenciesService = userCurrenciesService
        numericKeypad.bot = bot
        nextCurrencyUpdateProcessor.numericKeypad = numericKeypad
        nextCurrencyUpdateProcessor.expenseCache = expenseCache
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("next_currency")
        whenever(callbackQuery.from).thenReturn(from)
        whenever(from.id).thenReturn(2)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(message.chatId).thenReturn(1)
        whenever(expenseCache.contains(any())).thenReturn(true)
        whenever(expenseCache.get(any())).thenReturn(ExpenseCacheItem(1, 1000, currencies[0], SHOPPING))
    }

    @Test
    fun applies() {
        assertTrue(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notInCacheNotApplies() {
        whenever(expenseCache.contains(any())).thenReturn(false)
        assertFalse(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun anotherChatNotApplies() {
        whenever(expenseCache.get(any())).thenReturn(ExpenseCacheItem(111, 1000, currencies[0], SHOPPING))
        assertFalse(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        whenever(userCurrenciesService.getCurrencies(any())).thenReturn(currencies)
        whenever(expenseCache.get(any()))
            .thenReturn(ExpenseCacheItem(1, 1000, currencies[0], SHOPPING))
        whenever(expenseCache.updateCurrency(any(), any()))
            .thenReturn(ExpenseCacheItem(1, 1000, currencies[1], SHOPPING))
        nextCurrencyUpdateProcessor.process(update)
        verify(expenseCache).updateCurrency(any(), any())
        verify(bot).execute(any<EditMessageText>())
    }
}