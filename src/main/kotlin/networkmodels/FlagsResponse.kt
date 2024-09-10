package networkmodels

import com.google.gson.annotations.SerializedName

data class FlagsResponse(
    @SerializedName("imagePath") val imagePath: String? = null,
    @SerializedName("images") val images: List<Image> = listOf()
)
