package wasted.expense

import wasted.bot.Emoji
import java.time.Instant

data class Expense(
  val id: Long,
  val userId: Int,
  val groupId: Long,
  val telegramMessageId: Int?,
  val amount: Long,
  val currency: String,
  val category: Category,
  val date: Instant
) {

  enum class Category(val emoji: Emoji) {

    GROCERIES(Emoji.PIZZA),
    SHOPPING(Emoji.SHOPPING_CART),
    TRANSPORT(Emoji.STEAM_LOCOMOTIVE),
    HOME(Emoji.HOUSE),
    FEES(Emoji.BAR_CHART),
    ENTERTAINMENT(Emoji.BEER),
    TRAVEL(Emoji.AIRPLANE),
    HEALTH(Emoji.PILL),
    CAREER(Emoji.BOOKS),
    GIFTS(Emoji.GIFT),
    SPORT(Emoji.TABLE_TENNIS),
    HOBBIES(Emoji.TELESCOPE),
    BEAUTY(Emoji.LIPSTICK),
    OTHER(Emoji.GREY_QUESTION);

    companion object {
      fun fromName(name: String): Category? {
        return values().firstOrNull { it.name == name }
      }
    }
  }
}
