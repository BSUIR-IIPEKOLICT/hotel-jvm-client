package loshica.hotel.dtos

import com.google.gson.annotations.SerializedName

data class RoomDto(
    @SerializedName("type") val type: Int = 0,
    @SerializedName("description") var description: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("floor") var floor: Int = 0,
    @SerializedName("places") var places: Int = 0,
    @SerializedName("isFree") var isFree: Boolean = true
)
