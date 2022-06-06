package com.lis.safefilms.data.network

import com.lis.safefilms.BuildConfig.API_KEY
import com.lis.safefilms.data.KinopoiskAPIModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET("{filmId}")
    suspend fun getFilmData(@Path("filmId") filmId: Int): Response<KinopoiskAPIModel>

    companion object {
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"

        fun create(): Service{
            val httpClient = OkHttpClient.Builder()
                .addInterceptor{chain ->
                    val origin = chain.request()

                    val requestBuilder = origin.newBuilder()
                        .addHeader("X-Api-Key", API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .method(origin.method, origin.body)

                    val request = requestBuilder.build()

                    chain.proceed(request)
                }
                .retryOnConnectionFailure(true)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)
        }
    }
}
