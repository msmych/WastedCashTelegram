package wasted.expense

import wasted.bot.Emoji
import wasted.bot.Emoji.*

enum class ExpenseCategory(val emoji: Emoji) {
    GROCERIES(PIZZA),
    SHOPPING(SHOPPING_CART),
    TRANSPORT(STEAM_LOCOMOTIVE),
    HOME(HOUSE),
    FEES(DOLLAR),
    ENTERTAINMENT(GUITAR),
    TRAVEL(AIRPLANE),
    HEALTH(PILL),
    CAREER(BOOKS),
    GIFTS(GIFT),
    SPORT(TABLE_TENNIS),
    HOBBIES(TELESCOPE),
    BEAUTY(LIPSTICK),
    OTHER(BLACK_JOKER)
}