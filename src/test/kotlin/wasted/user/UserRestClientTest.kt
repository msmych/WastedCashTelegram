package wasted.user

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import wasted.expense.Expense
import wasted.expense.Expense.Category.OTHER
import java.util.*

internal class UserRestClientTest {

    companion object {

        private val userRestClient = UserRestClient()

        @JvmStatic @get:ClassRule
        val wireMockClassRule = WireMockClassRule()

        @JvmStatic val expense = Expense(1, 1234, 1234, 890, 1000, "USD", OTHER, Date())

        @BeforeClass
        @JvmStatic
        fun setUp() {
            userRestClient.apiToken = "1234"
            wireMockClassRule.stubFor(
                get(urlEqualTo("/user/1234/exists"))
                    .willReturn(aResponse().withStatus(200).withBody("true")))
            wireMockClassRule.stubFor(
                post(urlEqualTo("/user/1234"))
                    .willReturn(aResponse().withStatus(200)))
            wireMockClassRule.stubFor(
                get(urlEqualTo("/user/1234/currencies"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withBody(listOf("USD", "EUR", "RUB").toString())))
            wireMockClassRule.stubFor(
                patch(urlEqualTo("/user/1234/currency/usd"))
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withBody(listOf("EUR", "RUB").toString())))
        }
    }

    @Test
    fun existingUser() {
        assertTrue(userRestClient.existsUser(1234))
    }

    @Test
    fun creatingUser() {
        userRestClient.createUser(1234)
    }

    @Test
    fun gettingCurrencies() {
        assertEquals(
            listOf("USD", "EUR", "RUB").map { Currency.getInstance(it) },
            userRestClient.getUserCurrencies(1234)
        )
    }

    @Test
    fun toggleCurrency() {
        assertEquals(
            listOf("EUR", "RUB").map { Currency.getInstance(it) },
            userRestClient.toggleUserCurrency(1234, "usd")
        )
    }
}