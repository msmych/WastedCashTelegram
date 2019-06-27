package wasted.total

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.TotalKeypad
import wasted.total.Total.Type.ALL
import wasted.total.Total.Type.values

@Singleton
class TotalRecentUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var totalClient: TotalClient
    @Inject
    lateinit var totalKeypad: TotalKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return values()
            .filter { it != ALL }
            .map { it.name.toLowerCase() }
            .any { data == "total_$it" }
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val period = update.callbackQuery.data.substring("total_".length)
        totalKeypad.update(
            chatId,
            update.callbackQuery.message.messageId,
            totalClient.getRecentTotal(chatId, period),
            period)
    }
}