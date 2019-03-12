package wasted.bot

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class HelpUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/help"
    }

    override fun process(update: Update) {
        bot.execute(SendMessage(update.message.chatId, "https://telegra.ph/Wasted-cash-03-11"))
    }
}