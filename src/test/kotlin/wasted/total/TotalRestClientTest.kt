package wasted.total

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import wasted.bot.BotConfig
import wasted.expense.Expense.Category.SHOPPING
import wasted.rest.RestClient
import wasted.total.Total.Type.MONTH

internal class TotalRestClientTest {

  companion object {

    private val restClient = RestClient()
    private val totalRestClient = TotalRestClient()

    @JvmStatic
    @get:ClassRule
    val wireMockClassRule = WireMockClassRule()
    @JvmStatic
    val gson = Gson()

    @JvmStatic
    val total = Total(1, 2, 1000, "USD", SHOPPING)

    @BeforeClass
    @JvmStatic
    fun setUpStubs() {
      val botConfig = BotConfig()
      botConfig.apiBaseUrl = "http://localhost:8080"
      botConfig.apiToken = "1234"
      restClient.botConfig = botConfig
      totalRestClient.restClient = restClient
      wireMockClassRule.stubFor(
        get(urlPathEqualTo("/total/in/1/type/MONTH"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(gson.toJson(listOf(total)))
          )
      )
      wireMockClassRule.stubFor(
        get(urlPathEqualTo("/total/type/MONTH"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(gson.toJson(listOf(total)))
          )
      )
    }
  }

  @Test
  fun gettingTotal() {
    assertEquals(listOf(total), totalRestClient.total(1, MONTH, 1234))
  }

  @Test
  fun gettingTotals() {
    assertEquals(listOf(total), totalRestClient.totals(MONTH, 1234))
  }
}
