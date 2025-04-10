package com.teamoptimization

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.Jackson
import java.net.http.HttpClient

data class AcmeForecastingClientResult(val min: String, val max: String, val description: String) {
    companion object {
        val lens = Jackson.autoBody<AcmeForecastingClientResult>().toLens()
    }
}

interface Forecaster {
    fun acmeForecast(day: String, place: String): AcmeForecastingClientResult
}


class CachingAcmeForecasterClient constructor(private val delegate:Forecaster) : Forecaster {
//    private var cache mutableMapOf<String, , String>().also {
//
//    }
//
    override fun acmeForecast(day: String, place: String): AcmeForecastingClientResult {
        return delegate.acmeForecast(day, place)
    }
}

class AcmeForecasterClient constructor(private val httpClient: HttpHandler) : Forecaster {

    override fun acmeForecast(day: String, place: String): AcmeForecastingClientResult =
        this.httpClient(
            Request(Method.GET, "https://pqjbv9i19c.execute-api.eu-west-2.amazonaws.com/api/forecast?place=$place&day=$day")
        ).let { response ->
            if (response.status.successful) {
                AcmeForecastingClientResult.lens(response)
            } else {
                throw RuntimeException(response.toMessage())
            }
        }
}


