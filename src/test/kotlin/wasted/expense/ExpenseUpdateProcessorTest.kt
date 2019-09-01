package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.CategoriesKeypad
import java.time.Instant.now

internal class ExpenseUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val expenseClient = mock<ExpenseClient>()
  private val categoriesKeypad = CategoriesKeypad()

  private val expenseUpdateProcessor = ExpenseUpdateProcessor()

  private val update = mock<Update>()
  private val message = mock<Message>()
  private val user = mock<User>()

  @Before
  fun setUp() {
    expenseUpdateProcessor.bot = bot
    categoriesKeypad.bot = bot
    expenseUpdateProcessor.expenseClient = expenseClient
    expenseUpdateProcessor.categoriesKeypad = categoriesKeypad

    whenever(update.message).thenReturn(message)
    whenever(expenseClient.createExpense(any(), any()))
      .thenReturn(Expense(1, 1, 1, 2, 1000, "USD", SHOPPING, now()))
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
  fun wrong_not_applies() {
    whenever(message.text).thenReturn("/wrong")
    assertFalse(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun integer_applies() {
    whenever(message.text).thenReturn("/1000")
    assertTrue(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun finishes_with_period_not_applies() {
    whenever(message.text).thenReturn("/10.")
    assertFalse(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun one_digit_after_period_not_applies() {
    whenever(message.text).thenReturn("/10.0")
    assertFalse(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun three_digits_after_period_not_applies() {
    whenever(message.text).thenReturn("/10.000")
    assertFalse(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun nine_digits_not_applies() {
    whenever(message.text).thenReturn("/123456789")
    assertFalse(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun eight_digits_and_two_more_applies() {
    whenever(message.text).thenReturn("/12345678.90")
    assertTrue(expenseUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    expenseUpdateProcessor.process(update)
    verify(bot).execute(any<SendMessage>())
    verify(expenseClient).createExpense(any(), any())
    verify(bot).execute(any<EditMessageText>())
  }
}
