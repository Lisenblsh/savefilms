package com.lis.safefilms.ui.viewHolders

import android.view.View
import androidx.room.Database
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.data.room.KinopoiskAPIDB
import com.lis.safefilms.databinding.FilmCardBinding
import com.lis.safefilms.tools.DatabaseFun

class CurrentFilmViewHolder(
    private val itemView: View,
    private val idFilm: Int
    ) {
    private val database = DatabaseFun(FilmDatabase.getInstance(itemView.context))
    private lateinit var binding: FilmCardBinding

    suspend fun bind(){
        binding = FilmCardBinding.bind(itemView)
        val film: KinopoiskAPIDB = database.getFilm(idFilm)
        binding.showFilm(film)
    }

    private fun FilmCardBinding.showFilm(film: KinopoiskAPIDB) {

    }
}
