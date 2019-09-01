package wasted.keypad

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji
import wasted.expense.Expense.Category

fun ikb(label: String, data: String): InlineKeyboardButton {
    return InlineKeyboardButton(label).setCallbackData(data)
}

fun ikb(emoji: Emoji, data: String): InlineKeyboardButton {
    return ikb(emoji.code, data)
}

fun ikb(category: Category): InlineKeyboardButton {
    return ikb(category.emoji, category.name)
}
