package loshica.hotel.shared

import loshica.hotel.models.Room
import loshica.hotel.models.Type

object Default {
    private val TYPE = Type(id = -1, name = "example type", price = 123)

    val ROOM = Room(
        id = -1,
        type = TYPE,
        description = "no description",
        address = "no address",
        floor = -999,
        places = -1
    )
}