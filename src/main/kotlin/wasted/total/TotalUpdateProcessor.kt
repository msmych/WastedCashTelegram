package wasted.total

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.formatAmount
import java.util.*

@Singleton
class TotalUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot
    @Inject
    lateinit var totalClient: TotalClient

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/total" || text == "/total@${bot.botUsername}"
    }

    override fun process(update: Update) {
        bot.execute(SendMessage(update.message.chatId, "#total\n\n" +
                totalClient.getTotal(update.message.chatId)
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
            .setParseMode(MARKDOWN))
    }
}