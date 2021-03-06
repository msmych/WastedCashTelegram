package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CategoriesKeypad

@Singleton
class EditCategoryUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var expenseClient: ExpenseClient
  @Inject
  lateinit var categoriesKeypad: CategoriesKeypad

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return data == "edit-category"
      && expenseClient.expenseByGroupIdAndTelegramMessageId(
      callbackQuery.message.chatId,
      callbackQuery.message.messageId,
      callbackQuery.from.id
    )
      .userId == callbackQuery.from.id
  }

  override fun process(update: Update) {
    categoriesKeypad.update(
      update.callbackQuery.message.chatId,
      update.callbackQuery.message.messageId
    )
  }
}
