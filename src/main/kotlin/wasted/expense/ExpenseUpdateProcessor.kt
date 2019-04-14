package wasted.expense

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.OptionsKeypad
import wasted.rest.CreateExpenseRequest
import wasted.rest.RestClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseUpdateProcessor : UpdateProcessor {

    private val regex = Regex("/[0-9]{1,8}([/.][0-9]{2})?")

    @Inject lateinit var bot: TelegramLongPollingBot
    @Inject lateinit var restClient: RestClient
    @Inject lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return regex.matches(text)
    }

    override fun process(update: Update) {
        val chatId = update.message.chatId
        val messageId = bot.execute(SendMessage(chatId, "Un momento...")).messageId
        val expense = restClient.createExpense(CreateExpenseRequest(
            update.message.from.id,
            chatId,
            messageId,
            update.message.text.substring(1).replace(".", "").toLong()))
        optionsKeypad.update(expense)
    }
}