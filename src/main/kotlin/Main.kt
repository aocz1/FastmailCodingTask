import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.*
import kotlinx.coroutines.*
import networkmodels.FlagsResponse

// For testing purposes
fun main() = runBlocking {

    println("**Start**")

    val flagNames = getFlagNamesFromAPI()

    flagNames.forEach {
        println(it)
    }

    println("**End**")
}

// Function to be submitted
suspend fun getFlagNamesFromAPI(): List<String> {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    val flagsResponse = client
        .get("http://frontendtest.jobs.fastmail.com.user.fm/data.json")
        .body<FlagsResponse>()

    return flagsResponse
        .images
        .map {
            it.name ?: ""
        }
}
