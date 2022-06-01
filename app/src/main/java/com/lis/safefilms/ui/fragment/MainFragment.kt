package com.lis.safefilms.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lis.safefilms.R
import com.lis.safefilms.databinding.FragmentMainBinding
import com.lis.safefilms.di.Injection
import com.lis.safefilms.tools.Resource
import com.lis.safefilms.ui.FilmViewModel
import com.lis.safefilms.ui.ViewModelFactory

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: FilmViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentMainBinding.inflate(inflater, container, false)
            viewModel = ViewModelProvider(
                this,
                Injection.provideViewModelFactory(this)
            ).get(FilmViewModel::class.java)
        }

        viewModel.getFilmData(685246)
        viewModel.filmData.observe(viewLifecycleOwner){
            when (it){
                is Resource.Loading -> {
                    binding.test.text = "Загрузка"
                }
                is Resource.Success -> {
                    it.data.let { film ->
                        binding.test.text = film?.nameOriginal?: "имя блять где"
                    }
                }
                is Resource.Error -> {
                    it.message.let {
                        binding.test.text = "Ошибка ${it}"
                    }
                }

            }
        }
        return binding.root
    }
}