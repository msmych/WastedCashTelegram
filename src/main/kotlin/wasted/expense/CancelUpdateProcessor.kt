package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class CancelUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "cancel"
    }

    override fun process(update: Update) {
        expenseCache.remove(update.callbackQuery.from.id)
        bot.execute(EditMessageText()
            .setChatId(update.callbackQuery.message.chatId)
            .setMessageId(update.callbackQuery.message.messageId)
            .setText("Cancelled"))
    }
}