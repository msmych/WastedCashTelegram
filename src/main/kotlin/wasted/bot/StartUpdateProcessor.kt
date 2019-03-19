package wasted.bot

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.rest.RestClient

@Singleton
class StartUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/start"
    }

    override fun process(update: Update) {
        restClient.updateUser(update.message.from.id)
    }
}