package wasted.rest

import com.google.gson.Gson
import org.apache.http.client.fluent.Request.*
import org.apache.http.entity.ContentType.APPLICATION_JSON
import wasted.expense.Expense
import wasted.expense.clear.ClearExpenseType
import java.util.*
import javax.inject.Singleton

@Singleton
class RestHttpClient : RestClient {

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

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return gson.fromJson(
            Get("$baseUrl/user/$userId/currencies")
                .addHeader("api-token", apiToken)
                .execute().returnContent().asString(),
            Array<String>::class.java)
            .map { Currency.getInstance(it) }
    }

    override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
        return gson.fromJson(
            Patch("$baseUrl/user/$userId/currency/$currency")
                .addHeader("api-token", apiToken)
                .execute().returnContent().asString(),
            Array<String>::class.java)
            .map { Currency.getInstance(it) }
    }

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        return gson.fromJson(
            Get("$baseUrl/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
                .addHeader("api-token", apiToken)
                .execute().returnContent().asString(),
            Expense::class.java)
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
                APPLICATION_JSON)
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
