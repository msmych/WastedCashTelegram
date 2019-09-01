package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.OptionsKeypad

@Singleton
class OptionsUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var expenseClient: ExpenseClient
  @Inject
  lateinit var optionsKeypad: OptionsKeypad

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return data == "options"
      && expenseClient.expenseByGroupIdAndTelegramMessageId(
      callbackQuery.message.chatId,
      callbackQuery.message.messageId,
      callbackQuery.from.id
    )
      .userId == callbackQuery.from.id
  }

  override fun process(update: Update) {
    optionsKeypad.update(
      expenseClient.expenseByGroupIdAndTelegramMessageId(
        update.callbackQuery.message.chatId,
        update.callbackQuery.message.messageId,
        update.callbackQuery.from.id
      )
    )
  }
}
