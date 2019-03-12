package wasted.bot

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun ikb(emoji: Emoji, data: String): InlineKeyboardButton {
    return InlineKeyboardButton(emoji.code).setCallbackData(data)
}