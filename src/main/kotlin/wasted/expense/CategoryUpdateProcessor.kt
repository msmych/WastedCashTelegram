package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.*
import wasted.bot.ikb
import wasted.bot.update.processor.UpdateProcessor
import wasted.rest.RestClient
import java.util.*

@Singleton
class CategoryUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return ExpenseCategory.fromName(data) != null
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val expense = restClient.getExpense(fromId, chatId, messageId)
        val currency = Currency.getInstance(expense.currency)
        val category = ExpenseCategory.fromName(update.callbackQuery.data)!!
        restClient.saveExpense(fromId, chatId, messageId, expense.amount, currency, category)
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText("${formatAmount(expense.amount, currency)} for ${category.emoji.code}")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(InlineKeyboardMarkup().setKeyboard(listOf(
                listOf(ikb(X, "cancel"), ikb(E1234, "edit_amount"), ikb(BLACK_JOKER, "edit_category"))))))
    }
}