package wasted.expense.clear

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.Emoji.HEAVY_MULTIPLICATION_X
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseClient

@Singleton
class ClearByTypeUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var expenseClient: ExpenseClient

  @Inject
  lateinit var bot: TelegramLongPollingBot

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return ClearExpenseType.values().any { data == "clear$it" }
  }

  override fun process(update: Update) {
    val userId = update.callbackQuery.from.id
    val chatId = update.callbackQuery.message.chatId
    expenseClient.removeExpenseByType(
      chatId,
      ClearExpenseType.valueOf(update.callbackQuery.data.substring("clear".length)),
      userId
    )
    bot.execute(
      EditMessageText()
        .setChatId(chatId)
        .setMessageId(update.callbackQuery.message.messageId)
        .setText("${HEAVY_MULTIPLICATION_X.code} Cleared")
    )
  }
}
