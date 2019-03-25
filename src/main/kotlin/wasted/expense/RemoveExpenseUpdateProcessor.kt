package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.Emoji.HEAVY_MULTIPLICATION_X
import wasted.bot.update.processor.UpdateProcessor
import wasted.rest.RestClient

@Singleton
class RemoveExpenseUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        return data == "remove-expense"
                && restClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == fromId
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        restClient.removeExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText("${HEAVY_MULTIPLICATION_X.code} Cancelled"))
    }
}