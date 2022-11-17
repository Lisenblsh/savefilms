package com.lis.safefilms.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lis.safefilms.R
import com.lis.safefilms.databinding.FragmentCurrentFilmBinding

class CurrentFilmFragment : Fragment() {

    private lateinit var binding: FragmentCurrentFilmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!this::binding.isInitialized){
            binding = FragmentCurrentFilmBinding.inflate(inflater, container, false)
        }
        binding.showFilm()
        return binding.root
    }
    private fun FragmentCurrentFilmBinding.showFilm() {
        val itemView =LayoutInflater.from(context)
            .inflate(R.layout.film_card, root, false)

        root.addView(itemView)
    }
}
