package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.teamoptimization.AcmeForecasterClient
import moo
import org.http4k.client.JavaHttpClient
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
        val httpClient = JavaHttpClient()
        val forecastClient = AcmeForecasterClient(httpClient)
        val forecastData = forecastClient.acmeForecast(httpClient, "Monday", "Oxford")

//        assertThat(forecastData., containsSubstring("Expect temperatures in the range"))
        assertNotNull(forecastData)
    }
}
