package com.lis.safefilms.ui.adapters

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lis.safefilms.R
import com.lis.safefilms.data.room.KinopoiskAPIDB
import com.lis.safefilms.databinding.SmallFilmCardBinding
import com.lis.safefilms.tools.ImageFun
import kotlin.math.roundToInt

class FilmAdapter :
    ListAdapter<KinopoiskAPIDB, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    private lateinit var clickListener: OnItemClickListener

    fun setOnClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<KinopoiskAPIDB> =
            object : DiffUtil.ItemCallback<KinopoiskAPIDB>() {
                override fun areItemsTheSame(
                    oldItem: KinopoiskAPIDB,
                    newItem: KinopoiskAPIDB
                ): Boolean {
                    return oldItem.kinopoiskID == newItem.kinopoiskID
                }

                override fun areContentsTheSame(
                    oldItem: KinopoiskAPIDB,
                    newItem: KinopoiskAPIDB
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.small_film_card, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilmViewHolder) {
            holder.bind(currentList[position])
        }
    }

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var film: KinopoiskAPIDB? = null
        private var binding: SmallFilmCardBinding

        init {
            binding = SmallFilmCardBinding.bind(itemView)
            itemView.setOnClickListener {
                val id = film?.kinopoiskID
                if (id != null) {
                    clickListener.onItemClick(id)
                }
            }
        }

        fun bind(film: KinopoiskAPIDB) {
            this.film = film
            binding.showFilmData(film)
        }

        private fun SmallFilmCardBinding.showFilmData(film: KinopoiskAPIDB) {
            if (film.nameOriginal.isNullOrEmpty()) titleOrigin.visibility = View.GONE
            titleOrigin.text = film.nameOriginal
            titleRu.text = film.nameRu
            setDescription(film)
            ImageFun().setImage(film.posterURLPreview, posterImageview)
            setGenres(film)
            setCountries(film)
            setRatingColor(film)
            year.text = itemView.resources.getString(R.string.film_year, film.year)
            filmLength.text = film.filmLength?.let { getFilmLength(it) }
            setAgeRating(film)

        }

        private fun SmallFilmCardBinding.setDescription(film: KinopoiskAPIDB) {
            description.text = film.description
            description.setOnClickListener {
                val textView = it as TextView
                if (textView.maxLines == 2) {
                    textView.maxLines = Integer.MAX_VALUE
                } else {
                    textView.maxLines = 2
                }
            }
        }

        private fun SmallFilmCardBinding.setAgeRating(film: KinopoiskAPIDB) {
            if (!film.ratingAgeLimits.isNullOrEmpty()) {
                filmRatingAge.text = itemView.resources.getString(
                    R.string.film_rating_age,
                    film.ratingAgeLimits.replace("age", "")
                )
            }

        }

        private fun getFilmLength(length: Int) =
            itemView.resources
                .getString(R.string.film_length, length.div(60), length.rem(60))

        private fun SmallFilmCardBinding.setRatingColor(film: KinopoiskAPIDB) {
            when (film.ratingKinopoisk?.roundToInt()) {
                in 0..5 -> {
                    ratingKinopoisk.setTextColor(Color.RED)
                }
                in 7..10 -> {
                    ratingKinopoisk.setTextColor(Color.GREEN)
                }
            }
            ratingKinopoisk.text = film.ratingKinopoisk.toString()
        }

        private fun SmallFilmCardBinding.setCountries(film: KinopoiskAPIDB) {
            val countryList = film.countries?.split(',')
            if (!countryList.isNullOrEmpty()) {
                countryList.forEach { country ->
                    val textView = TextView(itemView.context)
                    textView.text = country
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    textView.id = View.generateViewId()
                    constraintLayout.addView(textView)
                    flowCountry.addView(textView)
                }
            }
        }

        private fun SmallFilmCardBinding.setGenres(film: KinopoiskAPIDB) {
            val genresList = film.genres?.split(',')
            if (!genresList.isNullOrEmpty()) {
                genresList.take(3).forEach { genre ->
                    val textView = TextView(itemView.context)
                    textView.text = genre
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    textView.id = View.generateViewId()
                    constraintLayout.addView(textView)
                    flowGenres.addView(textView)
                }
            }
        }
    }
}