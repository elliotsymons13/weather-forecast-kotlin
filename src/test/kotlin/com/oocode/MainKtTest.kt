package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.teamoptimization.AcmeForecasterClient
import moo
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull

abstract class AcmeDataContract {
    abstract val httpClient: HttpHandler

    @Test
    fun `can parse known response`() {

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Monday", "Oxford")

        assertNotNull(forecastData.min)
        assertNotNull(forecastData.max)
        assertNotNull(forecastData.description)

    }
}

class AcmeDataFake : AcmeDataContract() {
    private val hardCodedJsonResponse = "{\"min\": 5, \"max\": 12, \"description\": \"Cold and rainy\"}"
    override val httpClient: HttpHandler  = { Response(Status.OK).body(hardCodedJsonResponse) }

    @Test
    fun `can parse known responsex`() {

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Monday", "Oxford")

        assertEquals(forecastData.max, "12")
        assertEquals(forecastData.min, "5")
        assertEquals(forecastData.description, "Cold and rainy")

    }
}

class IntegrationTest : AcmeDataContract() {
    override val httpClient: HttpHandler = JavaHttpClient()
}

internal class MainKtTest {
    @Disabled
    @Test
    fun `moo is moo`() {
        assertThat(moo(), equalTo("boo"))
    }


}
