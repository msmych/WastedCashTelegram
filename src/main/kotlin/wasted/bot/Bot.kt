package wasted.bot

import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class Bot : TelegramLongPollingBot() {

    private val updateProcessors = HashSet<UpdateProcessor>()
    lateinit var token: String

    override fun getBotUsername(): String {
        return "WastedCashBot"
    }

    override fun getBotToken(): String {
        return token
    }

    override fun onUpdateReceived(update: Update?) {
        if (update == null)
            return
        updateProcessors
            .filter{ it.appliesTo(update) }
            .forEach{ it.process(update) }
        if (update.callbackQuery != null)
            execute(AnswerCallbackQuery().setCallbackQueryId(update.callbackQuery.id))
    }

    fun addUpdateProcessor(vararg updateProcessor: UpdateProcessor) {
        updateProcessors.addAll(updateProcessor)
    }
}
