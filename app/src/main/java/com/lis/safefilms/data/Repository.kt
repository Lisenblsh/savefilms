package com.lis.safefilms.data

import android.util.Log
import com.lis.safefilms.data.network.Service
import retrofit2.Response

class Repository(private val service: Service) {
    suspend fun getFilm(kinopoiskId: Int): Response<KinopoiskAPIModel> {
        val asd = service.getFilmData(kinopoiskId)
        Log.e("asd", "${kinopoiskId}\n${asd.body()}")
        return asd
    }
}