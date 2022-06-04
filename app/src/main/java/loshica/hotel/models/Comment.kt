package loshica.hotel.models

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("room") val room: Int = 0,
    @SerializedName("content") var content: String = ""
)
