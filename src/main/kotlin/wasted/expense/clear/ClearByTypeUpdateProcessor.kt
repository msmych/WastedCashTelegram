package wasted.expense.clear

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.rest.RestClient

@Singleton
class ClearByTypeUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient

    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "clearALL"
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        restClient.removeExpenseByType(
            chatId,
            ClearExpenseType.valueOf(update.callbackQuery.data.substring("clear".length)))
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(update.callbackQuery.message.messageId)
            .setText("Cleared"))
    }
}