package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad
import wasted.rest.CreateExpenseRequest
import wasted.rest.RestClient
import java.util.*

@Singleton
class WastedUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot
    @Inject
    lateinit var numericKeypad: NumericKeypad
    @Inject
    lateinit var restClient: RestClient

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/wasted"
    }

    override fun process(update: Update) {
        val chatId = update.message.chatId
        val messageId = bot.execute(SendMessage(chatId, "Un momento...")).messageId
        val expense = restClient.createExpense(CreateExpenseRequest(
            update.message.from.id, chatId, messageId))
        numericKeypad.update(chatId, messageId, expense.amount, Currency.getInstance(expense.currency))
    }
}