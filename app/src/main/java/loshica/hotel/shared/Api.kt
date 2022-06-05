package loshica.hotel.shared

import loshica.hotel.interfaces.IApi
import loshica.hotel.repositories.CommentRepository
import loshica.hotel.repositories.MainRepository
import loshica.hotel.repositories.RoomRepository
import loshica.hotel.repositories.TypeRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api : IApi {
    private const val BASE_URL = "https://iipekolict--hotel-sb.herokuapp.com"
    private var httpClient: Retrofit? = null

    private fun getClient(): Retrofit {
        if (httpClient == null) {
            httpClient = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return httpClient!!
    }

    override val roomRepository: RoomRepository
        get() = getClient().create(RoomRepository::class.java)

    override val typeRepository: TypeRepository
        get() = getClient().create(TypeRepository::class.java)

    override val commentRepository: CommentRepository
        get() = getClient().create(CommentRepository::class.java)

    override val mainRepository: MainRepository
        get() = getClient().create(MainRepository::class.java)
}