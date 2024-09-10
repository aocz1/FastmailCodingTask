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

    val flagNames = getFlagNamesFromAPI("WHITE PinK")

    flagNames.forEach {
        println(it)
    }

    println("**End**")
}

/** Function to be submitted
 * Assumptions:
 * 1. Search term is case-insensitive.
 * 2. Search terms are separated by spaces.
 */
suspend fun getFlagNamesFromAPI(
    searchTerm: String = ""
): List<String> {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    val flagsResponse = client
        .get("http://frontendtest.jobs.fastmail.com.user.fm/data.json")
        .body<FlagsResponse>()

    val searchTermsSplit = searchTerm.split(" ")

    return flagsResponse
        .images
        .filter { image ->
            if (searchTermsSplit.isNotEmpty()) {
                image.tags.any { tag ->
                    tag.lowercase() in searchTermsSplit.map { it.lowercase() }
                }
            } else {
                true
            }
        }
        .map {
            it.name ?: ""
        }
}
