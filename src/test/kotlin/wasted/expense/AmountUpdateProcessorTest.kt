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
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.NumericKeypad
import wasted.rest.RestClient
import java.util.*

internal class AmountUpdateProcessorTest {

    private val usd = Currency.getInstance("USD")

    private val numericKeypad = NumericKeypad()
    private val bot = mock<TelegramLongPollingBot>()
    private val restClient = mock<RestClient>()

    private val amountUpdateProcessor = AmountUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val from = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        amountUpdateProcessor.numericKeypad = numericKeypad
        amountUpdateProcessor.restClient = restClient
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.from).thenReturn(from)
        whenever(from.id).thenReturn(2)
        whenever(callbackQuery.data).thenReturn("12345")
        whenever(callbackQuery.message).thenReturn(message)
        whenever(message.chatId).thenReturn(1)
        whenever(restClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 2, 1, 2, 1000, "USD", SHOPPING, Date()))
    }

    @Test
    fun applies() {
        assertTrue(amountUpdateProcessor.appliesTo(update))
    }

    @Test
    fun over10notApplies() {
        whenever(callbackQuery.data).thenReturn("12345678901234")
        assertFalse(amountUpdateProcessor.appliesTo(update))
    }

    @Test
    fun lettersNotApplies() {
        whenever(callbackQuery.data).thenReturn("asdf")
        assertFalse(amountUpdateProcessor.appliesTo(update))
    }

    @Test
    fun noChangeNotApplies() {
        whenever(callbackQuery.data).thenReturn("1000")
        assertFalse(amountUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnExpenseNotApplies() {
        whenever(restClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 1, 2, 1000, "USD", SHOPPING, Date()))
        assertFalse(amountUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        amountUpdateProcessor.process(update)
        verify(restClient).updateExpense(any())
    }
}