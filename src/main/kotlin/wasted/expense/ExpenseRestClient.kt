package wasted.expense

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.apache.http.client.fluent.Request.Delete
import wasted.bot.BotConfig
import wasted.expense.clear.ClearExpenseType
import wasted.rest.RestClient
import java.lang.reflect.Type
import java.time.Instant
import java.time.Instant.now
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRestClient : ExpenseClient {

  private val gson = GsonBuilder().registerTypeAdapter(Instant::class.java, object : JsonDeserializer<Instant> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): Instant {
      if (json == null) return now()
      if (json.isJsonPrimitive) return Instant.parse(json.asJsonPrimitive.asString)
      val jsonObject = json.asJsonObject
      return Instant.ofEpochSecond(jsonObject.get("seconds").asJsonPrimitive.asLong)
        .plusNanos(jsonObject.get("nanos").asJsonPrimitive.asLong)
    }
  }).create()

  @Inject
  lateinit var botConfig: BotConfig
  @Inject
  lateinit var restClient: RestClient

  override fun expenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int, userId: Int): Expense {
    return restClient.getForObject(
      "/expense?groupId=$groupId&telegramMessageId=$telegramMessageId",
      userId,
      Expense::class.java,
      gson
    )
  }

  override fun createExpense(request: CreateExpenseRequest, userId: Int): Expense {
    return restClient.postForObject("/expense", userId, request, Expense::class.java, gson)
  }

  override fun updateExpense(expense: Expense, userId: Int) {
    restClient.put("/expense", userId, expense)
  }

  data class UpdateExpenseRequest(
    val id: Long,
    val amount: Long,
    val currency: String,
    val category: Expense.Category
  )

  override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
    Delete("${botConfig.apiBaseUrl}/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
      .addHeader("api-token", botConfig.apiToken)
      .execute()
  }

  override fun removeExpenseByType(groupId: Long, type: ClearExpenseType) {
    Delete("${botConfig.apiBaseUrl}/expense/in/$groupId/type/${type.name}")
      .addHeader("api-token", botConfig.apiToken)
      .execute()
  }
}
