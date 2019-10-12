package wasted.group

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import wasted.bot.BotConfig
import wasted.rest.RestClient

internal class GroupRestClientTest {

  companion object {
    private val groupRestClient = GroupRestClient()
    private val restClient = RestClient()

    @JvmStatic
    @get:ClassRule
    val wireMockClassRule = WireMockClassRule()

    @BeforeClass
    @JvmStatic
    fun setUp() {
      val botConfig = BotConfig()
      botConfig.apiBaseUrl = "http://localhost:8080"
      botConfig.apiToken = "1234"
      restClient.botConfig = botConfig
      groupRestClient.restClient = restClient
      wireMockClassRule.stubFor(
        get(urlEqualTo("/group/1234/monthly-report"))
          .willReturn(aResponse().withBody("true"))
      )
      wireMockClassRule.stubFor(
        get(urlEqualTo("/groups/monthly-report/ids"))
          .willReturn(aResponse().withBody("[1,2,3]"))
      )
    }
  }

  @Test
  fun getting_group_monthly_report() {
    assertTrue(groupRestClient.groupMonthlyReport(1234, 1234))
  }

  @Test
  fun getting_monthly_report_group_ids() {
    assertEquals(listOf(1L, 2L, 3L), groupRestClient.monthlyReportGroupsIds(1234))
  }
}
