package model

import com.google.gson.annotations.SerializedName

data class PixabayResponse(
    @SerializedName("hits")
    val hits: List<Hit>? = listOf(),
    @SerializedName("total")
    val total: Int? = 0,
    @SerializedName("totalHits")
    val totalHits: Int? = 0
)