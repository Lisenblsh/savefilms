package com.lis.safefilms.data.room

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lis.safefilms.data.KinopoiskAPIModel

@Database(entities = [KinopoiskAPIDB::class], version = 1, exportSchema = false)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmDao(): Dao

    companion object{
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getInstance(context: Context): FilmDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FilmDatabase::class.java,
                "film.db"
            ).fallbackToDestructiveMigration().build()
    }
}