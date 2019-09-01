package wasted.user

import com.google.gson.Gson
import com.google.inject.Singleton
import wasted.bot.BotConfig
import wasted.rest.RestClient
import java.util.*
import javax.inject.Inject

@Singleton
class UserRestClient : UserClient {

  private val gson = Gson()

  @Inject
  lateinit var botConfig: BotConfig
  @Inject
  lateinit var restClient: RestClient

  override fun existsUser(userId: Int): Boolean {
    return restClient.getForObject("/user/$userId/exists", userId, Boolean::class.java)
  }

  override fun createUser(userId: Int) {
    restClient.postForString("/user/$userId", userId)
  }

  override fun userCurrencies(userId: Int): List<Currency> {
    return restClient.getForObject("/user/$userId/currencies", userId, Array<String>::class.java)
      .map { Currency.getInstance(it) }
  }

  override fun userWhatsNew(userId: Int): Boolean {
    return restClient.getForObject("/user/$userId/whats-new", userId, Boolean::class.java)
  }

  override fun whatsNewSubscribedIds(userId: Int): List<Int> {
    return restClient.getForObject("/users/whats-new/ids", userId, Array<Int>::class.java).toList()
  }

  override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
    return restClient.patchForObject("/user/$userId/currency/$currency", userId, Array<String>::class.java)
      .map { Currency.getInstance(it) }
  }

  override fun toggleUserWhatsNew(userId: Int): Boolean {
    return restClient.patchForObject("/user/$userId/whats-new", userId, Boolean::class.java)
  }
}
