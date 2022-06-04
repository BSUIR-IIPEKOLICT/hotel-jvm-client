package loshica.hotel.repositories

import loshica.hotel.dtos.HealthCheckDto
import retrofit2.Response
import retrofit2.http.*

interface MainRepository {
    @GET("/")
    suspend fun healthCheck(): Response<HealthCheckDto>
}