package loshica.hotel.interfaces

import loshica.hotel.repositories.CommentRepository
import loshica.hotel.repositories.MainRepository
import loshica.hotel.repositories.RoomRepository
import loshica.hotel.repositories.TypeRepository

interface IApi {
    val roomRepository: RoomRepository
    val typeRepository: TypeRepository
    val commentRepository: CommentRepository
    val mainRepository: MainRepository
}