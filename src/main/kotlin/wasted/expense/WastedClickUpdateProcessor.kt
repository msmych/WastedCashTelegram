package wasted.expense

import com.google.inject.Inject
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad
import java.util.*

class WastedClickUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data.length <= 10
                && data.toLongOrNull() != null
                && expenseCache.contains(update.callbackQuery.from.id)
                && (expenseCache.get(update.callbackQuery.from.id).amount != 0L || data.toLong() != 0L)
    }

    override fun process(update: Update) {
        val item = expenseCache.updateAmount(update.callbackQuery.from.id, update.callbackQuery.data.toLong())
        numericKeypad.update(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId,
            item.amount,
            Currency.getInstance(item.currency))
    }
}