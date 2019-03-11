package wasted.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

class Bot(private val token: String) : TelegramLongPollingBot() {

    override fun getBotUsername(): String {
        return "WastedCashBot"
    }

    override fun getBotToken(): String {
        return token
    }

    override fun onUpdateReceived(update: Update?) {
        System.out.println(update)
    }
}
