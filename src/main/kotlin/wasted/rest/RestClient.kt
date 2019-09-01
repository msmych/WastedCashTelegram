package wasted.rest

import com.google.gson.Gson
import org.apache.commons.codec.digest.DigestUtils.sha256Hex
import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Request.*
import org.apache.http.client.fluent.Response
import org.apache.http.entity.ContentType.APPLICATION_JSON
import wasted.bot.BotConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestClient {

  @Inject
  lateinit var botConfig: BotConfig

  fun <T> getForObject(url: String, userId: Int, type: Class<T>, gson: Gson = Gson()): T {
    return gson.fromJson(getForString(url, userId), type)
  }

  fun getForString(url: String, userId: Int): String {
    return Get("${botConfig.apiBaseUrl}$url")
      .executeToString(userId)
  }

  fun <T> postForObject(url: String, userId: Int, body: Any, type: Class<T>, gson: Gson = Gson()): T {
    return gson.fromJson(
      Post("${botConfig.apiBaseUrl}$url")
        .bodyString(gson.toJson(body), APPLICATION_JSON)
        .executeToString(userId),
      type
    )
  }

  fun postForString(url: String, userId: Int): String {
    return Post("${botConfig.apiBaseUrl}$url")
      .executeToString(userId)
  }

  fun put(url: String, userId: Int, body: Any, gson: Gson = Gson()) {
    Put("${botConfig.apiBaseUrl}$url")
      .bodyString(gson.toJson(body), APPLICATION_JSON)
      .executeWithHeaders(userId)
  }

  fun delete(url: String, userId: Int) {
    Delete("${botConfig.apiBaseUrl}$url")
      .executeWithHeaders(userId)
  }

  fun <T> patchForObject(url: String, userId: Int, type: Class<T>, gson: Gson = Gson()): T {
    return gson.fromJson(
      Patch("${botConfig.apiBaseUrl}$url")
        .executeToString(userId),
      type
    )
  }

  private fun Request.executeToString(userId: Int): String {
    return this.executeWithHeaders(userId).returnContent().asString()
  }

  private fun Request.executeWithHeaders(userId: Int): Response {
    val userIdString = userId.toString()
    return this
      .addHeader("user-id", userIdString)
      .addHeader("api-token", sha256Hex("$userIdString${botConfig.apiToken}"))
      .execute()
  }
}
