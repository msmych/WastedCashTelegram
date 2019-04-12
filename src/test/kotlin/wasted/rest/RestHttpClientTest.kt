package wasted.rest

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import junit.framework.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test

internal class RestHttpClientTest {

    private val restHttpClient = RestHttpClient()

    companion object {

        @JvmStatic @get:ClassRule val wireMockClassRule = WireMockClassRule()

        @BeforeClass
        @JvmStatic
        fun setUp() {
            wireMockClassRule.stubFor(get(urlEqualTo("/user/1234/exists"))
                .willReturn(aResponse().withStatus(200).withBody("true")))
            wireMockClassRule.stubFor(post(urlEqualTo("/user/1234"))
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
}