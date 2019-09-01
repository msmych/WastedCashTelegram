package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.Expense.Category.OTHER
import wasted.keypad.OptionsKeypad

@Singleton
class CategoryUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var expenseClient: ExpenseClient
  @Inject
  lateinit var optionsKeypad: OptionsKeypad

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return Expense.Category.fromName(data) != null
      && expenseClient.expenseByGroupIdAndTelegramMessageId(
      callbackQuery.message.chatId,
      callbackQuery.message.messageId,
      callbackQuery.from.id
    )
      .userId == callbackQuery.from.id
  }

  override fun process(update: Update) {
    val chatId = update.callbackQuery.message.chatId
    val messageId = update.callbackQuery.message.messageId
    val userId = update.callbackQuery.from.id
    val lastExpense = expenseClient.expenseByGroupIdAndTelegramMessageId(chatId, messageId, userId)
    val expense = Expense(
      lastExpense.id,
      lastExpense.userId,
      lastExpense.groupId,
      lastExpense.telegramMessageId,
      lastExpense.amount,
      lastExpense.currency,
      Expense.Category.fromName(update.callbackQuery.data) ?: OTHER,
      lastExpense.date
    )
    expenseClient.updateExpense(expense)
    optionsKeypad.update(expense)
  }
}
