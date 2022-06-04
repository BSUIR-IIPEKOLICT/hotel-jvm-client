package loshica.hotel.models

import com.google.gson.annotations.SerializedName

data class Type(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("rooms") val rooms: List<Int> = emptyList(),
    @SerializedName("name") var name: String = "",
    @SerializedName("options") var options: String = "no options",
    @SerializedName("price") var price: Int = 0
)
