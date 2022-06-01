package com.lis.safefilms.data

data class KinopoiskAPIModel (
    val kinopoiskID: Int,
    val imdbID: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val nameOriginal: String? = null,
    val posterURL: String,
    val posterURLPreview: String,
    val coverURL: String? = null,
    val logoURL: String? = null,
    val reviewsCount: Int,
    val ratingGoodReview: Double? = null,
    val ratingGoodReviewVoteCount: Int? = null,
    val ratingKinopoisk: Double? = null,
    val ratingKinopoiskVoteCount: Int? = null,
    val ratingImdb: Double? = null,
    val ratingImdbVoteCount: Int? = null,
    val ratingFilmCritics: Double? = null,
    val ratingFilmCriticsVoteCount: Int? = null,
    val ratingAwait: Double? = null,
    val ratingAwaitCount: Int? = null,
    val ratingRFCritics: Int? = null,
    val ratingRFCriticsVoteCount: Int? = null,
    val webURL: String,
    val year: Int? = null,
    val filmLength: Int? = null,
    val slogan: String? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val editorAnnotation: String? = null,
    val isTicketsAvailable: Boolean,
    val productionStatus: ProductionStatus? = null,
    val type: Type,
    val ratingMPAA: String? = null,
    val ratingAgeLimits: String? = null,
    val countries: List<Country>,
    val genres: List<Genre>,
    val startYear: Any? = null,
    val endYear: Any? = null,
    val serial: Boolean? = null,
    val shortFilm: Boolean? = null,
    val completed: Boolean? = null,
    val hasImax: Boolean? = null,
    val has3D: Boolean? = null,
    val lastSync: String
)

data class Country (
    val country: String
)

data class Genre (
    val genre: String
)

enum class Type {
    FILM, VIDEO, TV_SERIES, MINI_SERIES, TV_SHOW
}

enum class ProductionStatus {
    FILMING, PRE_PRODUCTION, COMPLETED, ANNOUNCED, UNKNOWN, POST_PRODUCTION
}