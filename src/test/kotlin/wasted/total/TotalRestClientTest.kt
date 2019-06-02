package wasted.total

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import wasted.expense.Expense.Category.SHOPPING

internal class TotalRestClientTest {

    companion object {

        private val totalRestClient = TotalRestClient()

        @JvmStatic @get:ClassRule val wireMockClassRule = WireMockClassRule()
        @JvmStatic val gson = Gson()

        @JvmStatic val total = Total(1, 1000, "USD", SHOPPING)

        @BeforeClass
        @JvmStatic
        fun setUpStubs() {
            totalRestClient.apiToken = "1234"
            wireMockClassRule.stubFor(get(urlPathEqualTo("/total"))
                .withQueryParam("groupId", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(gson.toJson(listOf(total)))))
        }
    }

    @Test
    fun gettingTotal() {
        assertEquals(listOf(total), totalRestClient.getTotal(1))
    }
}