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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.CategoriesKeypad
import java.time.Instant.now

internal class EditCategoryUpdateProcessorTest {

  private val expenseClient = mock<ExpenseClient>()
  private val categoriesKeypad = CategoriesKeypad()
  private val bot = mock<TelegramLongPollingBot>()

  private val editCategoryUpdateProcessor = EditCategoryUpdateProcessor()

  private val update = mock<Update>()
  private val callbackQuery = mock<CallbackQuery>()
  private val user = mock<User>()

  @BeforeEach
  fun setUp() {
    editCategoryUpdateProcessor.expenseClient = expenseClient
    categoriesKeypad.bot = bot
    editCategoryUpdateProcessor.categoriesKeypad = categoriesKeypad
    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.data).thenReturn("edit-category")
    whenever(callbackQuery.from).thenReturn(user)
    whenever(user.id).thenReturn(1)
    whenever(callbackQuery.message).thenReturn(mock())
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 1, 2, 3, 1000, "USD", SHOPPING, now()))
  }

  @Test
  fun applies() {
    assertTrue(editCategoryUpdateProcessor.appliesTo(update))
  }

  @Test
  fun not_own_not_applies() {
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 111, 2, 3, 1000, "USD", SHOPPING, now()))
    assertFalse(editCategoryUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    editCategoryUpdateProcessor.process(update)
    verify(bot).execute(any<EditMessageReplyMarkup>())
  }
}
