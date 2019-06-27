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
import java.time.ZonedDateTime.now
import java.time.format.TextStyle
import java.util.*

@Singleton
class TotalKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, total: List<Total>, type: String = "month") {
        bot.execute(SendMessage(chatId, getText(total, type))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getInlineKeyboardMarkup(type)))
    }

    private fun getText(total: List<Total>, type: String): String {
        return "${getTitle(type)}\n\n" +
                total
                    .groupBy { it.currency }
                    .map { cur ->
                        val currencySum = cur.value.map { it.amount }.sum()
                        formatAmount(
                            currencySum,
                            Currency.getInstance(cur.key)
                        ) + "\n" +
                                cur.value.groupBy { it.category }
                                    .map { cat ->
                                        val categorySum = cat.value.map { it.amount }.sum()
                                        cat.key.emoji.code.repeat(1 + (10 * categorySum.toDouble() / currencySum.toDouble()).toInt()) + " " +
                                                formatAmount(categorySum, Currency.getInstance(cur.key))
                                    }.joinToString("\n")
                    }.joinToString("\n\n")
    }

    private fun getTitle(type: String): String {
        return "#total " + when (type) {
            "month" -> "${now().month.getDisplayName(TextStyle.FULL, Locale.US)} ${now().year}"
            "all" -> ""
            else -> throw IllegalArgumentException()
        }
    }

    private fun getInlineKeyboardMarkup(type: String): InlineKeyboardMarkup {
        return InlineKeyboardMarkup()
            .setKeyboard(listOf(listOf(
                ikb(if (type == "month") "${RADIO_BUTTON.code} Month" else "Month", "total_month"),
                ikb(if (type == "all") "${RADIO_BUTTON.code} All" else "All", "total_all"))))
    }

    fun update(chatId: Long, messageId: Int, total: List<Total>, type: String) {
        bot.execute(EditMessageText()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setText(getText(total, type))
            .setParseMode(MARKDOWN)
            .setReplyMarkup(getInlineKeyboardMarkup(type)))
    }
}