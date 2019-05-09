package wasted.rest

import com.google.gson.Gson
import org.apache.http.client.fluent.Request.*
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.apache.http.entity.StringEntity
import wasted.expense.Expense
import java.util.*
import javax.inject.Singleton

@Singleton
class RestHttpClient : RestClient {

    private val gson = Gson()
    private val baseUrl = "http://localhost:8080"

    override fun existsUser(userId: Int): Boolean {
        return Get("$baseUrl/user/$userId/exists").execute().returnContent().asString() == "true"
    }

    override fun createUser(userId: Int) {
        Post("$baseUrl/user/$userId").execute()
    }

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return gson.fromJson(
            Get("$baseUrl/user/$userId/currencies").execute().returnContent().asString(),
            Array<String>::class.java)
            .map { Currency.getInstance(it) }
    }

    override fun toggleCurrency(userId: Int, currency: String): List<Currency> {
        return gson.fromJson(
            Patch("$baseUrl/user/$userId/currency/$currency").execute().returnContent().asString(),
            Array<String>::class.java)
            .map { Currency.getInstance(it) }
    }

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        return gson.fromJson(
            Get("$baseUrl/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
                .execute().returnContent().asString(),
            Expense::class.java)
    }

    override fun createExpense(request: CreateExpenseRequest): Expense {
        return gson.fromJson(
            Post("$baseUrl/expense").body(StringEntity(gson.toJson(request), APPLICATION_JSON))
                .execute().returnContent().asString(),
            Expense::class.java)
    }

    override fun updateExpense(expense: Expense) {
        Put("$baseUrl/expense").body(StringEntity(gson.toJson(expense), APPLICATION_JSON))
    }

    override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
        Delete("$baseUrl/expense?groupId=$groupId&telegramMessageId=$telegramMessageId")
    }
}