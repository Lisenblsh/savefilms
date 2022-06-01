package com.lis.safefilms.data.network

import com.lis.safefilms.BuildConfig.API_KEY
import com.lis.safefilms.data.KinopoiskAPIModel
import retrofit2.http.GET

interface Service {

    @GET("{filmId}")
    suspend fun getFilmData(): KinopoiskAPIModel

    companion object {
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"
        val asd = API_KEY
    }
}
