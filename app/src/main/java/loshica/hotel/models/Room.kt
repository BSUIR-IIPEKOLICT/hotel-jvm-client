package loshica.hotel.models

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("type") val type: Type,
    @SerializedName("comments") var comments: MutableSet<Comment> = mutableSetOf(),
    @SerializedName("description") var description: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("floor") var floor: Int = 0,
    @SerializedName("places") var places: Int = 0,
    @SerializedName("isFree") var isFree: Boolean = true
)
