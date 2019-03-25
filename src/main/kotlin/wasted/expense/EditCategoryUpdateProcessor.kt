package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.E1234
import wasted.bot.Emoji.X
import wasted.bot.ikb
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.Expense.Category.*
import wasted.rest.RestClient

@Singleton
class EditCategoryUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "edit-category"
                && restClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        bot.execute(EditMessageReplyMarkup()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setReplyMarkup(getMarkup()))
    }

    private fun getMarkup(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup()
            .setKeyboard(listOf(
                listOf(ikb(GROCERIES), ikb(SHOPPING), ikb(TRANSPORT), ikb(HOME)),
                listOf(ikb(FEES), ikb(ENTERTAINMENT), ikb(TRAVEL), ikb(HEALTH)),
                listOf(ikb(CAREER), ikb(GIFTS), ikb(SPORT), ikb(HOBBIES)),
                listOf(ikb(X, "remove-expense"), ikb(BEAUTY), ikb(OTHER), ikb(E1234, "edit-amount"))))
    }
}