package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad

@Singleton
class WastedUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot
    @Inject
    lateinit var numericKeypad: NumericKeypad
    @Inject
    lateinit var expenseClient: ExpenseClient

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/wasted" || text == "/wasted@${bot.botUsername}"
    }

    override fun process(update: Update) {
        val chatId = update.message.chatId
        val messageId = bot.execute(SendMessage(chatId, "Un momento...")).messageId
        val expense = expenseClient.createExpense(
            CreateExpenseRequest(
                update.message.from.id, chatId, messageId
            )
        )
        numericKeypad.update(expense)
    }
}