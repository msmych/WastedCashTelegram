package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji.WHITE_CHECK_MARK
import wasted.bot.Emoji.X
import wasted.bot.ikb
import wasted.expense.Expense
import wasted.expense.formatAmount
import java.util.*

@Singleton
class NumericKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun update(expense: Expense) {
        val currency = Currency.getInstance(expense.currency)
        bot.execute(EditMessageText()
            .setChatId(expense.groupId)
            .setMessageId(expense.telegramMessageId)
            .setText("Wasted ${formatAmount(expense.amount, currency)} for ${expense.category.emoji.code}")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup(expense.amount, currency)))
    }

    private fun getMarkup(a: Long, c: Currency): InlineKeyboardMarkup {
        return InlineKeyboardMarkup().setKeyboard(listOf(
            listOf(ikbd(a, 1), ikbd(a, 2), ikbd(a, 3), ikb(c.symbol, "next-currency")),
            listOf(ikbd(a, 4), ikbd(a, 5), ikbd(a, 6), ikb("←", "${a / 10}")),
            listOf(ikbd(a, 7), ikbd(a, 8), ikbd(a, 9), ikb("AC", "0")),
            listOf(ikb(X, "remove-expense"), ikbd(a, 0), ikb("· 00 ·", "${a}00"), ikb(WHITE_CHECK_MARK, "options"))))
    }

    private fun ikbd(amount: Long, digit: Int): InlineKeyboardButton {
        return ikb("·  $digit  ·", "$amount$digit")
    }
}