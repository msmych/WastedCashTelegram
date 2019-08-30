package wasted.bot

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BotConfig {

  val apiBaseUrl = "https://wasted.cash"

  @Inject
  lateinit var apiToken: String
}
