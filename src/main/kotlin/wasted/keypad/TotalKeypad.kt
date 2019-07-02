package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Emoji.RADIO_BUTTON
import wasted.bot.ikb
import wasted.expense.formatAmount
import wasted.total.Total
import wasted.total.Total.Type
import wasted.total.Total.Type.ALL
import wasted.total.Total.Type.MONTH
import java.time.ZonedDateTime.now
import java.time.format.TextStyle
import java.util.*

@Singleton
class TotalKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, total: List<Total>, type: Type = MONTH) {
        bot.execute(SendMessage(chatId, getText(total, type))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getInlineKeyboardMarkup(type)))
    }

    private fun getText(total: List<Total>, type: Type): String {
        return "${getTitle(type)}\n\n" +
                total.groupBy { it.currency }
                    .map { cur ->
                        val curSum = cur.value.map { it.amount }.sum()
                        formatAmount(curSum, Currency.getInstance(cur.key)) + "\n" +
                                cur.value.sortedByDescending { it.amount }
                                    .joinToString("\n") {
                                    formatAmount(it.amount, Currency.getInstance(it.currency)) + " " +
                                            it.category.emoji.code
                                                .repeat(1 + (10 * it.amount.toDouble() / curSum.toDouble()).toInt())
                                }
                    }.joinToString("\n\n")
    }

    private fun getTitle(type: Type): String {
        return "#total " + when (type) {
            MONTH -> "${now().month.getDisplayName(TextStyle.FULL, Locale.US)} ${now().year}"
            ALL -> ""
        }
    }

    private fun getInlineKeyboardMarkup(type: Type): InlineKeyboardMarkup {
        return InlineKeyboardMarkup()
            .setKeyboard(listOf(listOf(
                ikb(if (type == MONTH) "${RADIO_BUTTON.code} Month" else "Month", "totalMONTH"),
                ikb(if (type == ALL) "${RADIO_BUTTON.code} All" else "All", "totalALL"))))
    }

    fun update(chatId: Long, messageId: Int, total: List<Total>, type: Type) {
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText(getText(total, type))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getInlineKeyboardMarkup(type)))
    }
}