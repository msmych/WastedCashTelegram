package wasted.rest

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import wasted.expense.Expense
import wasted.expense.Expense.Category.OTHER
import java.util.*

internal class RestHttpClientTest {

    private val restHttpClient = RestHttpClient()

    companion object {

        @JvmStatic @get:ClassRule val wireMockClassRule = WireMockClassRule()
        @JvmStatic val gson = Gson()

        @JvmStatic val expense = Expense(1, 1234, 1234, 890, 1000, "USD", OTHER, Date())
        @JvmStatic val createExpenseRequest = CreateExpenseRequest(1234, 1234, 890)

        @BeforeClass
        @JvmStatic
        fun setUp() {
            wireMockClassRule.stubFor(get(urlEqualTo("/user/1234/exists"))
                .willReturn(aResponse().withStatus(200).withBody("true")))
            wireMockClassRule.stubFor(post(urlEqualTo("/user/1234"))
                .willReturn(aResponse().withStatus(200)))
            wireMockClassRule.stubFor(get(urlEqualTo("/user/1234/currencies"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(listOf("USD", "EUR", "RUB").toString())))
            wireMockClassRule.stubFor(patch(urlEqualTo("/user/1234/currency/usd"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(listOf("EUR", "RUB").toString())))
            wireMockClassRule.stubFor(get(urlPathEqualTo("/expense"))
                .withQueryParam("groupId", equalTo("1234"))
                .withQueryParam("telegramMessageId", equalTo("890"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(gson.toJson(expense))))
            wireMockClassRule.stubFor(post(urlEqualTo("/expense"))
                .withRequestBody(equalToJson(gson.toJson(createExpenseRequest)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(gson.toJson(expense))))
            wireMockClassRule.stubFor(put(urlEqualTo("/expense"))
                .withRequestBody(equalToJson(gson.toJson(expense)))
                .willReturn(aResponse().withStatus(200)))
            wireMockClassRule.stubFor(delete(urlEqualTo("/expense"))
                .withQueryParam("groupId", equalTo("1234"))
                .withQueryParam("telegramMessageId", equalTo("890"))
                .willReturn(aResponse().withStatus(200)))
        }
    }

    @Test
    fun existingUser() {
        assertTrue(restHttpClient.existsUser(1234))
    }

    @Test
    fun creatingUser() {
        restHttpClient.createUser(1234)
    }

    @Test
    fun gettingCurrencies() {
        assertEquals(listOf("USD", "EUR", "RUB").map { Currency.getInstance(it) },
            restHttpClient.getUserCurrencies(1234))
    }

    @Test
    fun toggleCurrency() {
        assertEquals(listOf("EUR", "RUB").map { Currency.getInstance(it) },
            restHttpClient.toggleCurrency(1234, "usd"))
    }

    @Test
    fun gettingExpenseByGroupIdAndTelegramMessageId() {
        assertEquals(expense.toString(), restHttpClient.getExpenseByGroupIdAndTelegramMessageId(1234, 890).toString())
    }

    @Test
    fun creatingExpense() {
        assertEquals(expense.toString(), restHttpClient.createExpense(createExpenseRequest).toString())
    }

    @Test
    fun updatingExpense() {
        restHttpClient.updateExpense(expense)
    }

    @Test
    fun removingExpenseByGroupIdAndTelegramMessageId() {
        restHttpClient.removeExpenseByGroupIdAndTelegramMessageId(1234, 890)
    }
}