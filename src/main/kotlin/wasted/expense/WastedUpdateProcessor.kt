package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad
import java.util.*

@Singleton
class WastedUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/wasted"
    }

    override fun process(update: Update) {
        expenseCache.put(update.message.from.id, update.message.chatId, "USD", "FOOD")
        numericKeypad.send(update.message.chatId, 0, Currency.getInstance("USD"))
    }
}