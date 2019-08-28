package wasted.expense

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.apache.http.client.fluent.Request.*
import org.apache.http.entity.ContentType.APPLICATION_JSON
import wasted.bot.BotConfig
import wasted.expense.clear.ClearExpenseType
import java.lang.reflect.Type
import java.time.Instant
import java.time.Instant.now
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRestClient : ExpenseClient {

  private val gson = GsonBuilder().registerTypeAdapter(Instant::class.java, object : JsonDeserializer<Instant> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): Instant {
      return Instant.parse(json?.asJsonPrimitive?.asString ?: now().toString())
    }
  }).create()

  @Inject
  lateinit var botConfig: BotConfig

  override fun expenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
    return gson.fromJson(
      Get("${botConfig.apiBaseUrl}/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
        .addHeader("api-token", botConfig.apiToken)
        .execute().returnContent().asString(),
      Expense::class.java
    )
  }

  override fun createExpense(request: CreateExpenseRequest): Expense {
    return gson.fromJson(
      Post("${botConfig.apiBaseUrl}/expense")
        .addHeader("api-token", botConfig.apiToken)
        .bodyString(gson.toJson(request), APPLICATION_JSON)
        .execute().returnContent().asString(),
      Expense::class.java
    )
  }

  override fun updateExpense(expense: Expense) {
    Put("${botConfig.apiBaseUrl}/expense")
      .addHeader("api-token", botConfig.apiToken)
      .bodyString(
        gson.toJson(
          UpdateExpenseRequest(
            expense.id,
            expense.amount,
            expense.currency,
            expense.category
          )
        ),
        APPLICATION_JSON
      )
      .execute()
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
