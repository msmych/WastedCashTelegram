package wasted.bot

enum class Emoji(val code: String) {

    HOUSE("\uD83C\uDFE0"),
    PIZZA("\uD83C\uDF55"),
    BOOKS("\uD83D\uDCDA"),
    GIFT("\uD83C\uDF81"),
    SHOPPING_CART("\uD83D\uDED2"),
    AIRPLANE("✈️"),
    PILL("\uD83D\uDC8A"),
    STEAM_LOCOMOTIVE("\uD83D\uDE82"),
    GUITAR("\uD83C\uDFB8"),
    TELESCOPE("\uD83D\uDD2D"),
    TABLE_TENNIS("\uD83C\uDFD3"),
    LIPSTICK("\uD83D\uDC84"),
    BAR_CHART("📊"),

    WHITE_CHECK_MARK("✅"),
    BALLOT_BOX_WITH_CHECK("☑️"),
    RADIO_BUTTON("\uD83D\uDD18"),
    X("❌"),
    GREY_QUESTION("❔"),
    HEAVY_MULTIPLICATION_X("✖️"),
    ARROW_LEFT("⬅️"),
    ARROW_BACKWARD("◀️"),
    E1234("\uD83D\uDD22"),

    ONE("1️⃣"),
    TWO("2️⃣"),
    THREE("3️⃣"),
    FOUR("4️⃣"),
    FIVE("5️⃣"),
    SIX("6️⃣"),
    SEVEN("7️⃣"),
    EIGHT("8️⃣"),
    NINE("9️⃣"),
    ZERO("0️⃣");

    companion object {
        fun fromCode(code: String): Emoji? {
            return values().firstOrNull{ it.code == code }
        }
    }
}