package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.teamoptimization.AcmeForecasterClient
import moo
import org.http4k.client.JavaHttpClient
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull

internal class MainKtTest {
    @Disabled
    @Test
    fun `moo is moo`() {
        assertThat(moo(), equalTo("boo"))
    }

    @Test
    fun `can parse known response`() {
//        val httpClient = JavaHttpClient()
//        val forecastClient = AcmeForecasterClient(httpClient)

        val hardCodedJsonResponse = "{\"min\": 5, \"max\": 12, \"description\": \"Cold and rainy\"}"
        val forecastClient = AcmeForecasterClient({ Response(Status.OK).body(hardCodedJsonResponse) })
        val forecastData = forecastClient.acmeForecast("Monday", "Oxford")

        assertEquals(forecastData.max, "12")
        assertEquals(forecastData.min, "5")
        assertEquals(forecastData.description, "Cold and rainy")
    }
}
