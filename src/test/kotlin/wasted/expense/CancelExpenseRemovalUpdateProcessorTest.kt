package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
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
import java.util.*

internal class CancelExpenseRemovalUpdateProcessorTest {

    private val expenseClient = mock<ExpenseClient>()
    private val bot = mock<TelegramLongPollingBot>()
    private val optionsKeypad = OptionsKeypad()

    private val cancelExpenseRemovalUpdateProcessor = CancelExpenseRemovalUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val user = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    private fun setUp() {
        cancelExpenseRemovalUpdateProcessor.expenseClient = expenseClient
        cancelExpenseRemovalUpdateProcessor.optionsKeypad = optionsKeypad
        optionsKeypad.bot = bot

        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("cancel-expense-removal")
        whenever(callbackQuery.from).thenReturn(user)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(user.id).thenReturn(1)

        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 1, 2, 3, 1000, "USD", SHOPPING, Date()))
    }

    @Test
    fun applies() {
        assertTrue(cancelExpenseRemovalUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnNotApplies() {
        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 2, 3, 1000, "USD", SHOPPING, Date()))
        assertFalse(cancelExpenseRemovalUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        cancelExpenseRemovalUpdateProcessor.process(update)
        verify(bot).execute(any<EditMessageText>())
    }
}
