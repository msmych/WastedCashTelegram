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
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.OptionsKeypad
import wasted.user.UserClient
import java.util.*

internal class NextCurrencyOptionsUpdateProcessorTest {

    private val userClient = mock<UserClient>()
    private val expenseClient = mock<ExpenseClient>()
    private val optionsKeypad = OptionsKeypad()
    private val bot = mock<TelegramLongPollingBot>()

    private val nextCurrencyOptionsUpdateProcessor = NextCurrencyOptionsUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val user = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        nextCurrencyOptionsUpdateProcessor.userClient = userClient
        nextCurrencyOptionsUpdateProcessor.expenseClient = expenseClient
        nextCurrencyOptionsUpdateProcessor.optionsKeypad = optionsKeypad
        optionsKeypad.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("next-currency-option")
        whenever(callbackQuery.from).thenReturn(user)
        whenever(user.id).thenReturn(2)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(expenseClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, Date()))
        whenever(userClient.getUserCurrencies(any()))
            .thenReturn(listOf(Currency.getInstance("USD"), Currency.getInstance("EUR")))
    }

    @Test
    fun applies() {
        assertTrue(nextCurrencyOptionsUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnNotApplies() {
        whenever(expenseClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 3, 4, 1000, "USD", SHOPPING, Date()))
        assertFalse(nextCurrencyOptionsUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        nextCurrencyOptionsUpdateProcessor.process(update)
        verify(expenseClient).updateExpense(any())
        verify(bot).execute(any<EditMessageText>())
    }
}