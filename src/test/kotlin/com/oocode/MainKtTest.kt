package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.teamoptimization.AcmeForecasterClient
import com.teamoptimization.CachingAcmeForecasterClient
import moo
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull

class FakeAcmeData : HttpHandler {
    private val data = mutableMapOf<String, String>().also {
        it["Monday Oxford"] = "{\"min\": 5, \"max\": 12, \"description\": \"Cold and rainy\"}"
        it["Tuesday London"] = "{\"min\": 8, \"max\": 2, \"description\": \"Hot and rainy\"}"
    }

    override fun invoke(request: Request) =
        routes("/api/forecast" bind Method.GET to {
            val key = it.query("day") + " " + it.query("place")
            Response(OK).body(data[key].toString()) // routes gives http handler ...
        })(request) // ... and then we immediately invoke it on the request
}

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
    private var hardCodedJsonResponse = "{\"min\": 1, \"max\": 1, \"description\": \"meaningless\"}"
    override val httpClient: HttpHandler  = FakeAcmeData()

    @Test
    fun `can parse known response x`() {
        hardCodedJsonResponse = "{\"min\": 5, \"max\": 12, \"description\": \"Cold and rainy\"}"

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Monday", "Oxford")

        assertEquals(forecastData.max, "12")
        assertEquals(forecastData.min, "5")
        assertEquals(forecastData.description, "Cold and rainy")
    }

    @Test
    fun `can parse different known response`() {
        hardCodedJsonResponse = "{\"min\": 8, \"max\": 2, \"description\": \"Hot and rainy\"}"

        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast("Tuesday", "London")

        assertEquals(forecastData.max, "2")
        assertEquals(forecastData.min, "8")
        assertEquals(forecastData.description, "Hot and rainy")
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

class CachingTests {
    @Test
    fun `cache wrapper test`() {
        val httpClient: HttpHandler  = FakeAcmeData()

        val forecastClient = AcmeForecasterClient(httpClient)
        val cachingForecastClient = CachingAcmeForecasterClient(forecastClient)
        val forecastData = cachingForecastClient.acmeForecast("Tuesday", "London")

        assertEquals(forecastData.max, "2")
        assertEquals(forecastData.min, "8")
        assertEquals(forecastData.description, "Hot and rainy")
    }
}

