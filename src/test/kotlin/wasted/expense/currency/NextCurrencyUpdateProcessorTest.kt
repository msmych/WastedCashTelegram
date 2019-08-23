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
import wasted.expense.Expense
import wasted.expense.Expense.Category.SHOPPING
import wasted.expense.ExpenseClient
import wasted.expense.NextCurrencyUpdateProcessor
import wasted.keypad.NumericKeypad
import wasted.user.UserClient
import java.time.Instant.now
import java.util.*

internal class NextCurrencyUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val numericKeypad = NumericKeypad()
    private val userClient = mock<UserClient>()
    private val expenseClient = mock<ExpenseClient>()

    private val nextCurrencyUpdateProcessor = NextCurrencyUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()
    private val from = mock<User>()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        nextCurrencyUpdateProcessor.numericKeypad = numericKeypad
        nextCurrencyUpdateProcessor.userClient = userClient
        nextCurrencyUpdateProcessor.expenseClient = expenseClient
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("next-currency")
        whenever(callbackQuery.from).thenReturn(from)
        whenever(from.id).thenReturn(2)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(message.chatId).thenReturn(1)
        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 2, 1, 3, 1000, "USD", SHOPPING, now()))
        whenever(userClient.userCurrencies(any()))
            .thenReturn(listOf(Currency.getInstance("USD"), Currency.getInstance("EUR")))
    }

    @Test
    fun applies() {
        assertTrue(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnNotApplies() {
        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 1, 3, 1000, "USD", SHOPPING, now()))
        assertFalse(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun lastCurrencyNotApplies() {
        whenever(userClient.userCurrencies(any())).thenReturn(listOf(Currency.getInstance("USD")))
        assertFalse(nextCurrencyUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        whenever(userClient.userCurrencies(any()))
            .thenReturn(listOf(Currency.getInstance("USD")))
        nextCurrencyUpdateProcessor.process(update)
        verify(bot).execute(any<EditMessageText>())
    }
}
