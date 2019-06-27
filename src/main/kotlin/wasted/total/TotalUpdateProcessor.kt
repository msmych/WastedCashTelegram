package wasted.total

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.TotalKeypad

@Singleton
class TotalUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot
    @Inject
    lateinit var totalClient: TotalClient
    @Inject
    lateinit var totalKeypad: TotalKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/total" || text == "/total@${bot.botUsername}"
    }

    override fun process(update: Update) {
        val chatId = update.message.chatId
        totalKeypad.send(chatId, totalClient.getTotal(chatId))
    }
}