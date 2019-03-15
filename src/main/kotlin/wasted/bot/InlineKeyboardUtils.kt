package wasted.bot

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.expense.ExpenseCategory

fun ikb(label: String, data: String): InlineKeyboardButton {
    return InlineKeyboardButton(label).setCallbackData(data)
}

fun ikb(emoji: Emoji, data: String): InlineKeyboardButton {
    return ikb(emoji.code, data)
}

fun ikb(category: ExpenseCategory): InlineKeyboardButton {
    return ikb(category.emoji, category.name)
}