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
import java.util.*

@Singleton
class NumericKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, amount: Long, currency: Currency) {
        bot.execute(SendMessage(chatId, formatText(amount, currency))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup(amount, currency)))
    }

    fun update(chatId: Long, messageId: Int, amount: Long, currency: Currency) {
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText(formatText(amount, currency))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getMarkup(amount, currency)))
    }

    private fun formatText(amount: Long, currency: Currency): String {
        val sb = StringBuilder(amount.toString())
        if (amount < 100)
            sb.insert(0, '0')
        if (amount < 10)
            sb.insert(0, '0')
        sb.insert(sb.length - 2, '.')
        return "`$sb ${currency.symbol}`"
    }

    private fun getMarkup(amount: Long, currency: Currency): InlineKeyboardMarkup {
        return InlineKeyboardMarkup().setKeyboard(listOf(
            listOf(ikb(ONE, "${amount}1"), ikb(TWO, "${amount}2"), ikb(THREE, "${amount}3"), ikb(WHITE_CHECK_MARK, "ok")),
            listOf(ikb(FOUR, "${amount}4"), ikb(FIVE, "${amount}5"), ikb(SIX, "${amount}6"), ikb(ARROW_BACKWARD, "${amount / 10}")),
            listOf(ikb(SEVEN, "${amount}7"), ikb(EIGHT, "${amount}8"), ikb(NINE, "${amount}9"), ikb(ARROW_LEFT, "0")),
            listOf(ikb(currency.symbol, "next_currency"), ikb(ZERO, "${amount}0"), ikb(ZERO.code + ZERO.code, "${amount}00"), ikb(X, "cancel"))))
    }
}