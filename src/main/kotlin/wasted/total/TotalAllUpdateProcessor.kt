package wasted.total

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.TotalKeypad

@Singleton
class TotalAllUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var totalClient: TotalClient
    @Inject
    lateinit var totalKeypad: TotalKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "total_all"
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        totalKeypad.update(
            chatId,
            update.callbackQuery.message.messageId,
            totalClient.getTotal(chatId),
            "all")
    }
}