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
import com.lis.safefilms.databinding.FragmentFilmListBinding
import com.lis.safefilms.di.Injection
import com.lis.safefilms.tools.DatabaseFun
import com.lis.safefilms.ui.adapters.FilmAdapter
import kotlinx.coroutines.launch
import java.net.URL


class FilmListFragment : Fragment() {
    private lateinit var binding: FragmentFilmListBinding

    private val filmAdapter = FilmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentFilmListBinding.inflate(inflater, container, false)
        }
        binding.bindAdapter()
        return binding.root
    }


    private fun FragmentFilmListBinding.bindAdapter() {
        filmAdapter.setOnClickListener(object : FilmAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                //тут крч логика для отображения нажатого фильма
            }

            override fun onItemLongClick(id: Int) {
                //тут для долгого нажжатия, для открытия менюшки дополнительной
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            addElementToAdapter()
        }
    }
}

