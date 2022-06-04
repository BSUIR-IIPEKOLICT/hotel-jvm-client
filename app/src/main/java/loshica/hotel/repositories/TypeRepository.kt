package loshica.hotel.repositories

import loshica.hotel.dtos.DeleteDto
import loshica.hotel.models.Type
import retrofit2.Response
import retrofit2.http.*

interface TypeRepository {
    @GET("/types")
    suspend fun getAll(): Response<List<Type>>

    @GET("/types/{id}")
    suspend fun getOne(@Path("id") id: Int): Response<Type>

    @POST("/types")
    suspend fun create(@Body dto: Type): Response<Type>

    @PUT("/types/{id}")
    suspend fun change(@Path("id") id: Int, @Body dto: Type): Response<Type>

    @DELETE("/types/{id}")
    suspend fun delete(@Path("id") id: Int): Response<DeleteDto>
}