package loshica.hotel.repositories

import loshica.hotel.dtos.DeleteDto
import loshica.hotel.dtos.RoomDto
import loshica.hotel.models.Comment
import loshica.hotel.models.Room
import retrofit2.Response
import retrofit2.http.*

interface RoomRepository {
    @GET("/rooms")
    suspend fun getAll(): Response<List<Room>>

    @GET("/rooms/{id}")
    suspend fun getOne(@Path("id") id: Int): Response<Room>

    @GET("/rooms/{id}/comments")
    suspend fun getRoomComments(@Path("id") id: Int): Response<List<Comment>>

    @POST("/rooms")
    suspend fun create(@Body dto: RoomDto): Response<Room>

    @PUT("/rooms/{id}")
    suspend fun change(
        @Path("id") id: Int,
        @Query("status") status: String,
        @Body dto: RoomDto
    ): Response<Room>

    @DELETE("/rooms/{id}")
    suspend fun delete(@Path("id") id: Int): Response<DeleteDto>
}