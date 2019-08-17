package wasted.expense

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.Emoji.HEAVY_MULTIPLICATION_X
import wasted.bot.update.processor.UpdateProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfirmExpenseRemovalUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        return data == "confirm-expense-removal"
                && expenseClient.expenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == fromId
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        expenseClient.removeExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        bot.execute(
            EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText("${HEAVY_MULTIPLICATION_X.code} Removed"))
    }
}
