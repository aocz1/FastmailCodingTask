package networkmodels

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("name") val name: String? = null,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
    @SerializedName("tags") val tags: List<String> = listOf()
)
