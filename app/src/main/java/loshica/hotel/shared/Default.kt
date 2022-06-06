package loshica.hotel.shared

import loshica.hotel.models.Comment
import loshica.hotel.models.Room
import loshica.hotel.models.Type

object Default {
    private val TYPE = Type(name = "example type", price = 123)

    val ROOM = Room(
        type = TYPE,
        description = "example description",
        address = "example address",
        floor = 123,
        places = 123
    )

    val COMMENT = Comment(content = "example text")
}