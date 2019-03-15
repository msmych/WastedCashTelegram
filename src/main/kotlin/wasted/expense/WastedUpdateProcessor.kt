package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseCategory.OTHER
import wasted.keypad.NumericKeypad
import wasted.user.UserService

@Singleton
class WastedUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var numericKeypad: NumericKeypad
    @Inject
    lateinit var userService: UserService

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/wasted"
    }

    override fun process(update: Update) {
        val fromId = update.message.from.id
        val currency = userService.getCurrencies(fromId)[0]
        expenseCache.put(fromId, update.message.chatId, currency, OTHER)
        numericKeypad.send(update.message.chatId, 0, currency)
    }
}