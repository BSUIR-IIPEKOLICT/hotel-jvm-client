package loshica.hotel.shared

import loshica.hotel.models.Room
import loshica.hotel.models.Type

object Default {
    private val type = Type(id = -1, name = "example type", price = 123)

    val room = Room(
        id = -1,
        type = type,
        description = "no description",
        address = "no address",
        floor = -999,
        places = -1
    )
}