package loshica.hotel.models

import com.google.gson.annotations.SerializedName
import loshica.hotel.dtos.RoomDto

data class Room(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("type") val type: Type,
    @SerializedName("comments") var comments: MutableSet<Comment> = mutableSetOf(),
    @SerializedName("description") var description: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("floor") var floor: Int = 0,
    @SerializedName("places") var places: Int = 0,
    @SerializedName("isFree") var isFree: Boolean = true
) {

    fun toDto(): RoomDto = RoomDto(
        type = type.id,
        description = description,
        address = address,
        floor = floor,
        places = places,
        isFree = isFree
    )
}
