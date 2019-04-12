package wasted.rest

import org.apache.http.client.fluent.Request
import org.apache.http.client.fluent.Request.Post
import wasted.expense.Expense
import java.util.*
import javax.inject.Singleton

@Singleton
class RestHttpClient : RestClient {

    private val baseUrl = "http://localhost:8080"

    override fun existsUser(userId: Int): Boolean {
        return Request.Get("$baseUrl/user/$userId/exists").execute().returnContent().asString() == "true"
    }

    override fun createUser(userId: Int) {
        Post("$baseUrl/user/$userId").execute()
    }

    override fun getUserCurrencies(userId: Int): List<Currency> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toggleCurrency(userId: Int, currency: String): List<Currency> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createExpense(request: CreateExpenseRequest): Expense {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateExpense(expense: Expense) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}