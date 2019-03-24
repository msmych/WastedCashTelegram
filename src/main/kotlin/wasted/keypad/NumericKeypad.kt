package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.WHITE_CHECK_MARK
import wasted.bot.Emoji.X
import wasted.bot.ikb
import wasted.expense.formatAmount
import java.util.*

@Singleton
class NumericKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

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
            listOf(ikb("·  1  ·", "${a}1"), ikb("·  2  ·", "${a}2"), ikb("·  3  ·", "${a}3"), ikb(c.symbol, "next_currency")),
            listOf(ikb("·  4  ·", "${a}4"), ikb("·  5  ·", "${a}5"), ikb("·  6  ·", "${a}6"), ikb("←", "${a / 10}")),
            listOf(ikb("·  7  ·", "${a}7"), ikb("·  8  ·", "${a}8"), ikb("·  9  ·", "${a}9"), ikb("AC", "0")),
            listOf(ikb(X, "cancel"), ikb("·  0  ·", "${a}0"), ikb("· 00 ·", "${a}00"), ikb(WHITE_CHECK_MARK, "ok"))))
    }
}