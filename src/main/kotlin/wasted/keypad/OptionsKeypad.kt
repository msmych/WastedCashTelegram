package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.E1234
import wasted.bot.Emoji.X
import wasted.bot.ikb
import wasted.expense.Expense
import wasted.expense.formatAmount
import java.util.*

@Singleton
class OptionsKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun update(expense: Expense) {
        val currency = Currency.getInstance(expense.currency)
        bot.execute(EditMessageText()
            .setChatId(expense.groupId)
            .setMessageId(expense.telegramMessageId)
            .setText("Wasted ${formatAmount(expense.amount, currency)} for ${expense.category.emoji.code}")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(InlineKeyboardMarkup().setKeyboard(listOf(listOf(
                ikb(X, "remove-expense"),
                ikb(E1234, "edit-amount"),
                ikb(currency.symbol, "next-currency-option"),
                ikb(expense.category.emoji.code, "edit-category"))))))
    }
}