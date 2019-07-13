package wasted.expense

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CategoriesKeypad
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseUpdateProcessor : UpdateProcessor {

    private val regex = Regex("/[0-9]{1,8}([/.][0-9]{2})?")

    @Inject lateinit var bot: TelegramLongPollingBot
    @Inject lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var categoriesKeypad: CategoriesKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return regex.matches(text)
    }

    override fun process(update: Update) {
        val chatId = update.message.chatId
        val messageId = bot.execute(SendMessage(chatId, "Un momento...")).messageId
        val expense = expenseClient.createExpense(
            CreateExpenseRequest(
                update.message.from.id,
                chatId,
                messageId,
                parseAmount(update.message.text)
            )
        )
        categoriesKeypad.update(chatId, messageId, expense)
    }

    private fun parseAmount(str: String): Long {
        if (str.indexOf('.') != -1)
            return str.substring(1).replace(".", "").toLong()
        return str.substring(1).toLong() * 100
    }
}