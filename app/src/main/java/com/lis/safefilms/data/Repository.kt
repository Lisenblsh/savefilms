package com.lis.safefilms.data

import com.lis.safefilms.data.network.Service

class Repository(private val service: Service) {
    suspend fun getFilm(kinopoiskId: Int) = service.getFilmData(kinopoiskId)
}