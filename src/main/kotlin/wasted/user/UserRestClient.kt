package wasted.user

import com.google.gson.Gson
import com.google.inject.Singleton
import org.apache.http.client.fluent.Request.*
import wasted.bot.BotConfig
import java.util.*
import javax.inject.Inject

@Singleton
class UserRestClient : UserClient {

  private val gson = Gson()

  @Inject
  lateinit var botConfig: BotConfig

  override fun existsUser(userId: Int): Boolean {
    return Get("${botConfig.apiBaseUrl}/user/$userId/exists")
      .addHeader("api-token", botConfig.apiToken)
      .execute().returnContent().asString() == "true"
  }

  override fun createUser(userId: Int) {
    Post("${botConfig.apiBaseUrl}/user/$userId")
      .addHeader("api-token", botConfig.apiToken)
      .execute()
  }

  override fun userCurrencies(userId: Int): List<Currency> {
    return gson.fromJson(
      Get("${botConfig.apiBaseUrl}/user/$userId/currencies")
        .addHeader("api-token", botConfig.apiToken)
        .execute().returnContent().asString(),
      Array<String>::class.java
    )
      .map { Currency.getInstance(it) }
  }

  override fun userWhatsNew(userId: Int): Boolean {
    return Get("${botConfig.apiBaseUrl}/user/$userId/whats-new")
      .addHeader("api-token", botConfig.apiToken)
      .execute().returnContent().asString() == "true"
  }

  override fun whatsNewSubscribedIds(): List<Int> {
    return gson.fromJson(
      Get("${botConfig.apiBaseUrl}/users/whats-new/ids")
        .addHeader("api-token", botConfig.apiToken)
        .execute().returnContent().asString(),
      Array<Int>::class.java
    ).toList()
  }

  override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
    return gson.fromJson(
      Patch("${botConfig.apiBaseUrl}/user/$userId/currency/$currency")
        .addHeader("api-token", botConfig.apiToken)
        .execute().returnContent().asString(),
      Array<String>::class.java
    )
      .map { Currency.getInstance(it) }
  }

  override fun toggleUserWhatsNew(userId: Int): Boolean {
    return Patch("${botConfig.apiBaseUrl}/user/$userId/whats-new")
      .addHeader("api-token", botConfig.apiToken)
      .execute().returnContent().asString() == "true"
  }
}
