package wasted.bot

import javax.inject.Singleton

@Singleton
class BotConfig {

  lateinit var apiBaseUrl: String
  lateinit var apiToken: String
}
