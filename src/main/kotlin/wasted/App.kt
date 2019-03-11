package wasted

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val telegramBotApi = TelegramBotsApi()
    val bot = Bot(args[0])
    telegramBotApi.registerBot(bot)
}