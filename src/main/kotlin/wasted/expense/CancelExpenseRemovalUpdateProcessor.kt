package wasted.expense

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.OptionsKeypad
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CancelExpenseRemovalUpdateProcessor : UpdateProcessor {

    @Inject lateinit var expenseClient: ExpenseClient
    @Inject lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        return data == "cancel-expense-removal" &&
                expenseClient.expenseByGroupIdAndTelegramMessageId(
                    callbackQuery.message.chatId,
                    callbackQuery.message.messageId)
                    .userId == fromId
    }

    override fun process(update: Update) {
        optionsKeypad.update(expenseClient.expenseByGroupIdAndTelegramMessageId(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId))
    }

}
