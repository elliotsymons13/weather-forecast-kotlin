package com.teamoptimization

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.Jackson

data class AcmeForecastingClientResult(val min: String, val max: String, val description: String) {
    companion object {
        val lens = Jackson.autoBody<AcmeForecastingClientResult>().toLens()
    }
}

interface Forecaster {
    fun acmeForecast(day: String, place: String): AcmeForecastingClientResult
// TODO in reality would want to have our own ForecastResult type, rather than tightly coupling with the AcmeForecastingClientResult provided by a third party library
}


class CachingAcmeForecasterClient constructor(private val delegate:Forecaster) : Forecaster {
    private var cache = mutableMapOf<String,AcmeForecastingClientResult>()

    override fun acmeForecast(day: String, place: String): AcmeForecastingClientResult {
        // check if cache already contains key
        val key = "$day $place"
        if (cache.containsKey(key)) { // if it exists, return cache
            val nullishDefault = AcmeForecastingClientResult("", "", "")
            return cache.getOrDefault(key, nullishDefault)
        } else { //otherwise, use the delegate
            val newforecast = delegate.acmeForecast(day, place)
            cache.set(key, newforecast)
            return newforecast
        }
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


