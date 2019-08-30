package wasted.bot

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BotConfig {

  val apiBaseUrl = "https://localhost"

  @Inject
  lateinit var apiToken: String
}
