package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji
import wasted.bot.Emoji.*
import wasted.bot.ikb
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseCategory.*
import wasted.rest.RestClient

@Singleton
class OkUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "ok" && expenseCache.contains(callbackQuery.from.id)
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val item = expenseCache.remove(fromId)
        restClient.saveExpense(fromId, update.callbackQuery.message.chatId, item.amount, item.currency, item.category)
        bot.execute(EditMessageText()
            .setChatId(update.callbackQuery.message.chatId)
            .setMessageId(update.callbackQuery.message.messageId)
            .setText("Wasted ${formatAmount(item.amount, item.currency)}")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup()))
    }

    private fun getMarkup(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup()
            .setKeyboard(listOf(
                listOf(ikb(GROCERIES), ikb(SHOPPING), ikb(TRANSPORT), ikb(HOME)),
                listOf(ikb(FEES), ikb(ENTERTAINMENT), ikb(TRAVEL), ikb(HEALTH)),
                listOf(ikb(CAREER), ikb(GIFTS), ikb(SPORT), ikb(HOBBIES)),
                listOf(ikb(X, "cancel"), ikb(BEAUTY), ikb(OTHER), ikb(E1234, "edit"))))
    }
}