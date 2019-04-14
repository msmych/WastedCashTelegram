package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.OptionsKeypad
import wasted.rest.RestClient
import java.util.*

internal class ExpenseUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val restClient = mock<RestClient>()
    private val optionsKeypad = OptionsKeypad()

    private val expenseUpdateProcessor = ExpenseUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()
    private val user = mock<User>()

    @Before
    fun setUp() {
        expenseUpdateProcessor.bot = bot
        optionsKeypad.bot = bot
        expenseUpdateProcessor.restClient = restClient
        expenseUpdateProcessor.optionsKeypad = optionsKeypad

        whenever(update.message).thenReturn(message)
        whenever(restClient.createExpense(any()))
            .thenReturn(Expense(1, 1, 1, 2, 1000, "USD", SHOPPING, Date()))
        whenever(message.text).thenReturn("/10.00")
        whenever(message.chatId).thenReturn(1)
        whenever(message.from).thenReturn(user)
        whenever(bot.execute(any<SendMessage>())).thenReturn(message)
    }

    @Test
    fun applies() {
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun wrongNotApplies() {
        whenever(message.text).thenReturn("/wrong")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun integerApplies() {
        whenever(message.text).thenReturn("/1000")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun finishesWithPeriodNotApplies() {
        whenever(message.text).thenReturn("/10.")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun oneDigitAfterPeriodNotApplies() {
        whenever(message.text).thenReturn("/10.0")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun threeDigitsAfterPeriodNotApplies() {
        whenever(message.text).thenReturn("/10.000")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun nineDigitsNotApplies() {
        whenever(message.text).thenReturn("/123456789")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun eightDigitsAndTwoMoreApplies() {
        whenever(message.text).thenReturn("/12345678.90")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        expenseUpdateProcessor.process(update)
        verify(bot).execute(any<SendMessage>())
        verify(restClient).createExpense(any())
        verify(bot).execute(any<EditMessageText>())
    }
}