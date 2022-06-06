package com.lis.safefilms.tools

import androidx.room.withTransaction
import com.lis.safefilms.data.KinopoiskAPIModel
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.data.room.KinopoiskAPIDB

class DatabaseFun(private val database: FilmDatabase) {
    private suspend fun insertFilmToBD(film: KinopoiskAPIDB) {
        database.filmDao().insertFilm(film)
    }

    suspend fun writeToDB(filmAPI: KinopoiskAPIModel) {
        insertFilmToBD(convertToDB(filmAPI))
    }

    private fun convertToDB(filmAPI: KinopoiskAPIModel): KinopoiskAPIDB {
        return KinopoiskAPIDB(
            kinopoiskID = filmAPI.kinopoiskId,
            imdbID = filmAPI.imdbId,
            nameRu = filmAPI.nameRu,
            nameEn = filmAPI.nameEn,
            nameOriginal = filmAPI.nameOriginal,
            posterURL = filmAPI.posterUrl,
            posterURLPreview = filmAPI.posterUrlPreview,
            coverURL = filmAPI.coverUrl,
            logoURL = filmAPI.logoUrl,
            reviewsCount = filmAPI.reviewsCount,
            ratingGoodReview = filmAPI.ratingGoodReview,
            ratingGoodReviewVoteCount = filmAPI.ratingGoodReviewVoteCount,
            ratingKinopoisk = filmAPI.ratingKinopoisk,
            ratingKinopoiskVoteCount = filmAPI.ratingKinopoiskVoteCount,
            ratingImdb = filmAPI.ratingImdb,
            ratingImdbVoteCount = filmAPI.ratingImdbVoteCount,
            ratingFilmCritics = filmAPI.ratingFilmCritics,
            ratingFilmCriticsVoteCount = filmAPI.ratingFilmCriticsVoteCount,
            ratingAwait = filmAPI.ratingAwait,
            ratingAwaitCount = filmAPI.ratingAwaitCount,
            ratingRFCritics = filmAPI.ratingRFCritics,
            ratingRFCriticsVoteCount = filmAPI.ratingRFCriticsVoteCount,
            webURL = filmAPI.webUrl,
            year = filmAPI.year,
            filmLength = filmAPI.filmLength,
            slogan = filmAPI.slogan,
            description = filmAPI.description,
            shortDescription = filmAPI.shortDescription,
            editorAnnotation = filmAPI.editorAnnotation,
            isTicketsAvailable = filmAPI.isTicketsAvailable,
            productionStatus = filmAPI.productionStatus,
            type = filmAPI.type,
            ratingMPAA = filmAPI.ratingMpaa,
            ratingAgeLimits = filmAPI.ratingAgeLimits,
            countries = filmAPI.countries.joinToString(separator = ", ") { it.country},
            genres = filmAPI.genres.joinToString(separator = ", ") {it.genre},
            startYear = filmAPI.startYear,
            endYear = filmAPI.endYear,
            serial = filmAPI.serial,
            shortFilm = filmAPI.shortFilm,
            completed = filmAPI.completed,
            hasImax = filmAPI.hasImax,
            has3D = filmAPI.has3D,
            lastSync = filmAPI.lastSync,
        )
    }

    suspend fun getFilms(): List<KinopoiskAPIDB> {
        return database.withTransaction {
            return@withTransaction database.filmDao().getFilms()
        }
    }
}