package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.E1234
import wasted.bot.Emoji.X
import wasted.bot.ikb
import wasted.expense.Expense
import wasted.expense.Expense.Category.*
import wasted.expense.formatAmount
import java.util.*

@Singleton
class CategoriesKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun update(chatId: Long, messageId: Int) {
        bot.execute(EditMessageReplyMarkup()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setReplyMarkup(markup()))
    }

    private fun markup(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup()
            .setKeyboard(listOf(
                listOf(ikb(GROCERIES), ikb(SHOPPING), ikb(TRANSPORT), ikb(HOME)),
                listOf(ikb(FEES), ikb(ENTERTAINMENT), ikb(TRAVEL), ikb(HEALTH)),
                listOf(ikb(CAREER), ikb(GIFTS), ikb(SPORT), ikb(HOBBIES)),
                listOf(ikb(X, "remove-expense"), ikb(BEAUTY), ikb(OTHER), ikb(E1234, "edit-amount"))))
    }

    fun update(chatId: Long, messageId: Int, expense: Expense) {
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText("${formatAmount(expense.amount, Currency.getInstance(expense.currency))} ${expense.category.emoji.code}")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(markup()))
    }
}