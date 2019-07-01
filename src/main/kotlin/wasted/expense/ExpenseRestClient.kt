package wasted.expense

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.inject.Singleton
import org.apache.http.client.fluent.Request.*
import org.apache.http.entity.ContentType.APPLICATION_JSON
import wasted.expense.clear.ClearExpenseType

@Singleton
class ExpenseRestClient : ExpenseClient {
    private val gson = Gson()

    private val baseUrl = "http://localhost:8080"

    lateinit var apiToken: String

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        return gson.fromJson(
            Get("$baseUrl/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
                .addHeader("api-token", apiToken)
                .execute().returnContent().asString(),
            Expense::class.java)
    }

    override fun getTelegramMessageIds(groupId: Long): List<Int> {
        return gson.fromJson(
            Get("$baseUrl/expense/telegramMessageIds?groupId=$groupId")
                .addHeader("api-token", apiToken)
                .execute().returnContent().asString(),
            object: TypeToken<List<Int>>(){}.type)
    }

    override fun createExpense(request: CreateExpenseRequest): Expense {
        return gson.fromJson(
            Post("$baseUrl/expense")
                .addHeader("api-token", apiToken)
                .bodyString(gson.toJson(request), APPLICATION_JSON)
                .execute().returnContent().asString(),
            Expense::class.java)
    }

    override fun updateExpense(expense: Expense) {
        Put("$baseUrl/expense")
            .addHeader("api-token", apiToken)
            .bodyString(
                gson.toJson(
                    UpdateExpenseRequest(
                        expense.id,
                        expense.amount,
                        expense.currency,
                        expense.category)),
                APPLICATION_JSON
            )
            .execute()
    }

    data class UpdateExpenseRequest(val id: Long,
                                    val amount: Long,
                                    val currency: String,
                                    val category: Expense.Category)

    override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
        Delete("$baseUrl/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
            .addHeader("api-token", apiToken)
            .execute()
    }

    override fun removeExpenseByType(groupId: Long, type: ClearExpenseType) {
        Delete("$baseUrl/expense/in/$groupId/type/${type.name}")
            .addHeader("api-token", apiToken)
            .execute()
    }
}