package wasted.total

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.http.client.fluent.Request.Get
import wasted.bot.BotConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalRestClient : TotalClient {

  private val gson = Gson()

  @Inject
  lateinit var botConfig: BotConfig

  override fun total(groupId: Long, type: Total.Type): List<Total> {
    return gson.fromJson(Get("${botConfig.apiBaseUrl}/total/in/$groupId/type/$type")
      .addHeader("api-token", botConfig.apiToken)
      .execute().returnContent().asString(),
      object : TypeToken<List<Total>>() {}.type
    )
  }

  override fun totals(type: Total.Type): List<Total> {
    return gson.fromJson(Get("${botConfig.apiBaseUrl}/total/type/$type")
      .addHeader("api-token", botConfig.apiToken)
      .execute().returnContent().asString(),
      object : TypeToken<List<Total>>() {}.type
    )
  }
}
