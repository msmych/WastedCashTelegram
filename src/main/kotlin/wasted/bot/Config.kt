package wasted.bot

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Config {

  @Inject
  lateinit var apiToken: String
}
