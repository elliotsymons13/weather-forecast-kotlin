import com.teamoptimization.AcmeForecasterClient
import com.teamoptimization.CachingAcmeForecasterClient
import com.teamoptimization.Forecaster
import org.http4k.client.JavaHttpClient

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw RuntimeException("Must specify Day and Place")
    }

    val forecastingClient = CachingAcmeForecasterClient(AcmeForecasterClient(JavaHttpClient()))
    printForecast(args[0], args[1], forecastingClient)
    printForecast(args[0], args[1], forecastingClient)
    printForecast(args[0], args[1], forecastingClient)
}

private fun printForecast(day: String, place: String, forecastingClient: Forecaster) {
    val acmeForecast = forecastingClient.acmeForecast(day, place)

    val emoji =
        if (acmeForecast.min.toInt() < 5) {
            "❄️"
        } else {
            if (acmeForecast.max.toInt() < 15) {
                "🧥"
            } else {
                "🔥"
            }
        }
    println(message(emoji, acmeForecast.description, acmeForecast.min.toInt(), acmeForecast.max.toInt()))
}

private fun message(emoji: String, description: String, min: Int, max: Int) =
    "$description $emoji Expect temperatures in the range $min-${max}°C."

fun moo() = "boo"
