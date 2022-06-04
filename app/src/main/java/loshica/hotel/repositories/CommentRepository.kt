package loshica.hotel.repositories

import loshica.hotel.dtos.DeleteDto
import loshica.hotel.models.Comment
import retrofit2.Response
import retrofit2.http.*

interface CommentRepository {
    @POST("/comments")
    suspend fun create(@Body dto: Comment): Response<Comment>

    @PUT("/comments/{id}")
    suspend fun change(@Path("id") id: Int, @Body dto: Comment): Response<Comment>

    @DELETE("/comments/{id}")
    suspend fun delete(@Path("id") id: Int): Response<DeleteDto>
}