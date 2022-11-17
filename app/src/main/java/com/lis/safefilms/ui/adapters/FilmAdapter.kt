package com.lis.safefilms.ui.adapters

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lis.safefilms.R
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.data.room.KinopoiskAPIDB
import com.lis.safefilms.databinding.SmallFilmCardBinding
import com.lis.safefilms.tools.DatabaseFun
import com.lis.safefilms.tools.ImageFun
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FilmAdapter :
    ListAdapter<KinopoiskAPIDB, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(id: Int)

        fun onItemLongClick(id: Int)
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
            itemView.setOnLongClickListener {
                binding.flowButtons.visibility = if (binding.flowButtons.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                val id = film?.kinopoiskID
                if (id != null) {
                    clickListener.onItemLongClick(id)
                }
                return@setOnLongClickListener true
            }
        }

        fun bind(film: KinopoiskAPIDB) {
            binding.flowButtons.visibility = View.GONE
            this.film = film
            binding.showFilmData(film)
        }

        private fun SmallFilmCardBinding.showFilmData(film: KinopoiskAPIDB) {
            if (film.nameOriginal.isNullOrEmpty()) titleOrigin.visibility = View.GONE
            titleOrigin.text = film.nameOriginal
            titleRu.text = film.nameRu
            setDescription(film)
            ImageFun().setImage(film.posterURLPreview, posterImageview)
            genres.text = film.genres?.replace(',', ' ')
            country.text = film.countries?.replace(',', ' ')
            setRatingColor(film)
            year.text = itemView.resources.getString(R.string.film_year, film.year)
            filmLength.text = film.filmLength?.let { getFilmLength(it) }
            setAgeRating(film)

            initOpenButton(film)
            initDeleteButton(film)

        }

        private fun SmallFilmCardBinding.initDeleteButton(film: KinopoiskAPIDB) {
            deleteFilm.setOnClickListener {
                itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    DatabaseFun(FilmDatabase.getInstance(itemView.context)).deleteFilm(film.kinopoiskID)
                    submitList(currentList)
                }
            }
        }

        private fun SmallFilmCardBinding.initOpenButton(film: KinopoiskAPIDB) {
            openInApp.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.data = Uri.parse(film.webURL)
                    startActivity(itemView.context,intent,null)
                } catch (e: Exception) {

                }
            }
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
    }
}