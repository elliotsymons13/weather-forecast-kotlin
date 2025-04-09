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
    abstract var httpClient: HttpHandler

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
    private var hardCodedJsonResponse = "{\"min\": 1, \"max\": 1, \"description\": \"meaningless\"}"
    override var httpClient: HttpHandler  = { Response(Status.OK).body(hardCodedJsonResponse) }

    @Test
    fun `can parse known response x`() {
        hardCodedJsonResponse = "{\"min\": 5, \"max\": 12, \"description\": \"Cold and rainy\"}"
        httpClient = { Response(Status.OK).body(hardCodedJsonResponse) }

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Monday", "Oxford")

        assertEquals(forecastData.max, "12")
        assertEquals(forecastData.min, "5")
        assertEquals(forecastData.description, "Cold and rainy")

    }

    @Test
    fun `can parse different known response`() {
        hardCodedJsonResponse = "{\"min\": 8, \"max\": 2, \"description\": \"Hot and rainy\"}"
        httpClient = { Response(Status.OK).body(hardCodedJsonResponse) }

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Tuesday", "London")

        assertEquals(forecastData.max, "2")
        assertEquals(forecastData.min, "8")
        assertEquals(forecastData.description, "Hot and rainy")

    }
}

class IntegrationTest : AcmeDataContract() {
    override var httpClient: HttpHandler = JavaHttpClient()
}

internal class MainKtTest {
    @Disabled
    @Test
    fun `moo is moo`() {
        assertThat(moo(), equalTo("boo"))
    }


}
