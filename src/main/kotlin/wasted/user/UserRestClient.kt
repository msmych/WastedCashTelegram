package wasted.user

import com.google.gson.Gson
import com.google.inject.Singleton
import org.apache.http.client.fluent.Request.*
import java.util.*

@Singleton
class UserRestClient : UserClient {

  private val gson = Gson()

  private val baseUrl = "http://localhost:8080"

  lateinit var apiToken: String

  override fun existsUser(userId: Int): Boolean {
    return Get("$baseUrl/user/$userId/exists")
      .addHeader("api-token", apiToken)
      .execute().returnContent().asString() == "true"
  }

  override fun createUser(userId: Int) {
    Post("$baseUrl/user/$userId")
      .addHeader("api-token", apiToken)
      .execute()
  }

  override fun userCurrencies(userId: Int): List<Currency> {
    return gson.fromJson(
      Get("$baseUrl/user/$userId/currencies")
        .addHeader("api-token", apiToken)
        .execute().returnContent().asString(),
      Array<String>::class.java
    )
      .map { Currency.getInstance(it) }
  }

  override fun userWhatsNew(userId: Int): Boolean {
    return Get("$baseUrl/user/$userId/whats-new")
      .addHeader("api-token", apiToken)
      .execute().returnContent().asString() == "true"
  }

  override fun whatsNewSubscribedIds(): List<Int> {
    return gson.fromJson(
      Get("$baseUrl/users/whats-new/ids")
        .addHeader("api-token", apiToken)
        .execute().returnContent().asString(),
      Array<Int>::class.java
    ).toList()
  }

  override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
    return gson.fromJson(
      Patch("$baseUrl/user/$userId/currency/$currency")
        .addHeader("api-token", apiToken)
        .execute().returnContent().asString(),
      Array<String>::class.java
    )
      .map { Currency.getInstance(it) }
  }
}
