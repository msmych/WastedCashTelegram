package wasted.expense

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.gson.Gson
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import wasted.bot.BotConfig
import wasted.expense.Expense.Category.OTHER
import wasted.expense.clear.ClearExpenseType.ALL
import wasted.rest.RestClient
import java.time.Instant.now

internal class ExpenseRestClientTest {

  companion object {

    private val expenseRestClient = ExpenseRestClient()
    private val restClient = RestClient()

    @JvmStatic
    @get:ClassRule
    val wireMockClassRule = WireMockClassRule()
    @JvmStatic
    val gson = Gson()

    @JvmStatic
    val expense = Expense(1, 1234, 1234, 890, 1000, "USD", OTHER, now())
    @JvmStatic
    val createExpenseRequest = CreateExpenseRequest(1234, 1234, 890)

    @BeforeClass
    @JvmStatic
    fun setUp() {
      val botConfig = BotConfig()
      botConfig.apiBaseUrl = "http://localhost:8080"
      botConfig.apiToken = "1234"
      expenseRestClient.restClient = restClient
      restClient.botConfig = botConfig
      wireMockClassRule.stubFor(
        get(urlPathEqualTo("/expense"))
          .withQueryParam("groupId", equalTo("1234"))
          .withQueryParam("telegramMessageId", equalTo("890"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(gson.toJson(expense))
          )
      )
      wireMockClassRule.stubFor(
        post(urlEqualTo("/expense"))
          .withRequestBody(equalToJson(gson.toJson(createExpenseRequest)))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(gson.toJson(expense))
          )
      )
      wireMockClassRule.stubFor(
        put(urlEqualTo("/expense"))
          .withRequestBody(equalToJson(gson.toJson(expense)))
          .willReturn(aResponse().withStatus(200))
      )
      wireMockClassRule.stubFor(
        delete(urlEqualTo("/expense"))
          .withQueryParam("groupId", equalTo("1234"))
          .withQueryParam("telegramMessageId", equalTo("890"))
          .willReturn(aResponse().withStatus(200))
      )
      wireMockClassRule.stubFor(
        delete(urlEqualTo("/expense/in/1234/type/ALL"))
          .willReturn(aResponse().withStatus(200))
      )
    }
  }

  @Test
  fun gettingExpenseByGroupIdAndTelegramMessageId() {
    assertEquals(
      expense.toString(),
      expenseRestClient.expenseByGroupIdAndTelegramMessageId(1234, 890, 1234).toString()
    )
  }

  @Test
  fun creatingExpense() {
    assertEquals(
      expense.toString(),
      expenseRestClient.createExpense(createExpenseRequest, 1234).toString()
    )
  }

  @Test
  fun updatingExpense() {
    expenseRestClient.updateExpense(expense, 1234)
  }

  @Test
  fun removingExpenseByGroupIdAndTelegramMessageId() {
    expenseRestClient.removeExpenseByGroupIdAndTelegramMessageId(1234, 890, 1234)
  }

  @Test
  fun removingExpenseByType() {
    expenseRestClient.removeExpenseByType(1234, ALL, 1234)
  }
}
