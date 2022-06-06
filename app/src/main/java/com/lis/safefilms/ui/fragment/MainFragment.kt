package com.lis.safefilms.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lis.safefilms.data.room.FilmDatabase
import com.lis.safefilms.databinding.FragmentMainBinding
import com.lis.safefilms.di.Injection
import com.lis.safefilms.tools.DatabaseFun
import com.lis.safefilms.ui.adapters.FilmAdapter
import kotlinx.coroutines.launch
import java.net.URL


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val filmAdapter = FilmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentMainBinding.inflate(inflater, container, false)
        }
        binding.bindAdapter()
        return binding.root
    }


    private fun FragmentMainBinding.bindAdapter() {
        filmAdapter.setOnClickListener(object : FilmAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                //тут крч логика для отображения нажатого фильма
            }
        })

        filmList.adapter = filmAdapter
        filmList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        lifecycleScope.launch {
            addElementToAdapter()
        }
    }

    private suspend fun addElementToAdapter() {
        val films = DatabaseFun(FilmDatabase.getInstance(requireContext())).getFilms()
        filmAdapter.submitList(films)
    }

    private fun intent() {
        val intent = activity?.intent
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                handleSendText(intent)
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val filmId = URL(it).path
                .substringBeforeLast('/') //get without last '/'
                .substringAfterLast('/') //get film id
                .toIntOrNull() //convert film id to int
            lifecycleScope.launch {
                getFIlm(filmId)
            }
        }
    }

    private suspend fun getFIlm(filmId: Int?) {
        if (filmId != null) {
            val response = Injection.provideRepository().getFilm(filmId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    DatabaseFun(FilmDatabase.getInstance(requireContext())).writeToDB(body)
                }
            }
        }
    }
}

