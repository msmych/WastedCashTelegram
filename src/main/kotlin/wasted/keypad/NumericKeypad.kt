package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.*
import wasted.bot.ikb
import wasted.expense.formatAmount
import java.util.*

@Singleton
class NumericKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, amount: Long, currency: Currency) {
        bot.execute(SendMessage(chatId, formatAmount(amount, currency))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup(amount, currency)))
    }

    fun update(chatId: Long, messageId: Int, amount: Long, currency: Currency) {
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText(formatAmount(amount, currency))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup(amount, currency)))
    }

    private fun getMarkup(a: Long, c: Currency): InlineKeyboardMarkup {
        return InlineKeyboardMarkup().setKeyboard(listOf(
            listOf(ikb(ONE, "${a}1"), ikb(TWO, "${a}2"), ikb(THREE, "${a}3"), ikb(WHITE_CHECK_MARK, "ok")),
            listOf(ikb(FOUR, "${a}4"), ikb(FIVE, "${a}5"), ikb(SIX, "${a}6"), ikb(ARROW_BACKWARD, "${a / 10}")),
            listOf(ikb(SEVEN, "${a}7"), ikb(EIGHT, "${a}8"), ikb(NINE, "${a}9"), ikb(ARROW_LEFT, "0")),
            listOf(ikb(c.symbol, "next_currency"), ikb(ZERO, "${a}0"), ikb(ZERO.code + ZERO.code, "${a}00"), ikb(X, "cancel"))))
    }
}