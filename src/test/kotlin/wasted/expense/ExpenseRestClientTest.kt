package wasted.expense

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.gson.Gson
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import wasted.expense.Expense.Category.OTHER
import wasted.expense.clear.ClearExpenseType.ALL
import java.util.*

internal class ExpenseRestClientTest {

    companion object {

        private val expenseRestClient = ExpenseRestClient()

        @JvmStatic @get:ClassRule
        val wireMockClassRule = WireMockClassRule()
        @JvmStatic val gson = Gson()

        @JvmStatic val expense = Expense(1, 1234, 1234, 890, 1000, "USD", OTHER, Date())
        @JvmStatic val createExpenseRequest = CreateExpenseRequest(1234, 1234, 890)

        @BeforeClass
        @JvmStatic
        fun setUp() {
            expenseRestClient.apiToken = "1234"
            wireMockClassRule.stubFor(
                get(urlPathEqualTo("/expense"))
                    .withQueryParam("groupId", equalTo("1234"))
                    .withQueryParam("telegramMessageId", equalTo("890"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withBody(gson.toJson(expense))))
            wireMockClassRule.stubFor(
                get(urlPathEqualTo("/expense/telegramMessageIds"))
                    .withQueryParam("groupId", equalTo("1234"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withBody(gson.toJson(listOf(1, 2, 3, 4, 5)))))
            wireMockClassRule.stubFor(
                post(urlEqualTo("/expense"))
                    .withRequestBody(equalToJson(gson.toJson(createExpenseRequest)))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withBody(gson.toJson(expense))))
            wireMockClassRule.stubFor(
                put(urlEqualTo("/expense"))
                    .withRequestBody(equalToJson(gson.toJson(expense)))
                    .willReturn(aResponse().withStatus(200)))
            wireMockClassRule.stubFor(
                delete(urlEqualTo("/expense"))
                    .withQueryParam("groupId", equalTo("1234"))
                    .withQueryParam("telegramMessageId", equalTo("890"))
                    .willReturn(aResponse().withStatus(200)))
            wireMockClassRule.stubFor(
                delete(urlEqualTo("/expense/in/1234/type/ALL"))
                    .willReturn(aResponse().withStatus(200)))
        }
    }

    @Test
    fun gettingExpenseByGroupIdAndTelegramMessageId() {
        assertEquals(
            expense.toString(),
            expenseRestClient.getExpenseByGroupIdAndTelegramMessageId(1234, 890).toString()
        )
    }

    @Test
    fun gettingTelegramMessageIds() {
        assertArrayEquals(
            arrayOf(1, 2, 3, 4, 5),
            expenseRestClient.getTelegramMessageIds(1234).toTypedArray())
    }

    @Test
    fun creatingExpense() {
        assertEquals(
            expense.toString(),
            expenseRestClient.createExpense(createExpenseRequest).toString()
        )
    }

    @Test
    fun updatingExpense() {
        expenseRestClient.updateExpense(expense)
    }

    @Test
    fun removingExpenseByGroupIdAndTelegramMessageId() {
        expenseRestClient.removeExpenseByGroupIdAndTelegramMessageId(1234, 890)
    }

    @Test
    fun removingExpenseByType() {
        expenseRestClient.removeExpenseByType(1234, ALL)
    }
}