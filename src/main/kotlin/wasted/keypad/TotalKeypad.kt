package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import wasted.expense.formatAmount
import wasted.total.Total
import java.util.*

@Singleton
class TotalKeypad {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, total: List<Total>, type: String = "all") {
        bot.execute(
            SendMessage(chatId, "${getTitle(type)}\n\n" +
                    total
                        .groupBy { it.currency }
                        .map { cur ->
                            val currencySum = cur.value.map { it.amount }.sum()
                            formatAmount(
                                currencySum,
                                Currency.getInstance(cur.key)) + "\n" +
                                    cur.value.groupBy { it.category }
                                        .map { cat ->
                                            val categorySum = cat.value.map { it.amount }.sum()
                                            cat.key.emoji.code.repeat(1 + (10 * categorySum.toDouble() / currencySum.toDouble()).toInt()) + " " +
                                                    formatAmount(categorySum, Currency.getInstance(cur.key))
                                        }.joinToString("\n")
                        }.joinToString("\n\n"))
                .setParseMode(ParseMode.MARKDOWN))
    }

    private fun getTitle(type: String): String {
        return "#total " + when (type) {
            "all" -> ""
            else -> throw IllegalArgumentException()
        }
    }
}