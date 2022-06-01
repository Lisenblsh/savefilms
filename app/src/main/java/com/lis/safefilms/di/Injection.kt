package com.lis.safefilms.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.lis.safefilms.data.Repository
import com.lis.safefilms.data.network.Service
import com.lis.safefilms.ui.FilmViewModel
import com.lis.safefilms.ui.ViewModelFactory

object Injection {
    private fun provideRepository(): Repository{
        return Repository(Service.create())
    }
    fun provideViewModelFactory(
        owner: SavedStateRegistryOwner
    ): ViewModelProvider.Factory{
        return ViewModelFactory(
            owner,
            provideRepository()
        )
    }
}