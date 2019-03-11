package wasted.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class Bot(private val token: String) : TelegramLongPollingBot() {

    override fun getBotUsername(): String {
        return "WastedCashBot"
    }

    override fun getBotToken(): String {
        return token
    }

    override fun onUpdateReceived(update: Update?) {
        if (update == null)
            return
        val message = update.message
        val text = message.text ?: return
        if (text == "/help")
            help(message.chatId)
    }

    private fun help(chatId: Long) {
        execute(SendMessage(chatId, "https://telegra.ph/Wasted-cash-03-11"))
    }
}
