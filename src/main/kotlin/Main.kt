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

    val flagNames = getFlagNamesFromAPI("WHITE $IS_PORTRAIT_SEARCH_TERM")

    flagNames.forEach {
        println(it)
    }

    println("**End**")
}

/** Function to be submitted
 * Assumptions:
 * 1. Search term is case-insensitive.
 * 2. Search terms are separated by spaces.
 * 3. Portrait means height > width, landscape is width > height.
 * If both are equal, then it is neither portrait or landscape.
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

    val searchTermsSplit = searchTerm.split(" ").map { it.lowercase() }

    return flagsResponse
        .images
        .filter { image ->
            if (searchTermsSplit.contains(IS_PORTRAIT_SEARCH_TERM)) {
                return@filter (image.height ?: 0) > (image.width ?: 0)
            }

            if (searchTermsSplit.contains(IS_LANDSCAPE_SEARCH_TERM)) {
                return@filter (image.width ?: 0) > (image.height ?: 0)
            }

            true
        }
        .filter { image ->
            if (searchTermsSplit.isNotEmpty()) {

                val onlyColourSearchTerms = searchTermsSplit
                    .filter { term ->
                        (term == IS_PORTRAIT_SEARCH_TERM).not() &&
                                (term == IS_LANDSCAPE_SEARCH_TERM).not()
                    }

                image
                    .tags
                    .map { it.lowercase() }
                    .containsAll(onlyColourSearchTerms)
            } else {
                true
            }
        }
        .map {
            it.name ?: ""
        }
}

const val IS_PORTRAIT_SEARCH_TERM = "is:portrait"
const val IS_LANDSCAPE_SEARCH_TERM = "is:landscape"
