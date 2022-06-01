package com.lis.safefilms.data.room

import androidx.room.*
import androidx.room.Dao
import com.lis.safefilms.data.KinopoiskAPIModel
import org.intellij.lang.annotations.Language

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: KinopoiskAPIDB)

    @Query("select *from film")
    fun getFilms(): List<KinopoiskAPIDB>

    @Query("select *from film where kinopoiskID = :kinopoiskID ")
    fun getFilm(kinopoiskID: Int): KinopoiskAPIDB

    @Language("RoomSql")
    @Query("delete from film where kinopoiskID = :kinopoiskID")
    suspend fun deleteFilm(kinopoiskID: Int)
}