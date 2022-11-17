package com.lis.safefilms.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.lis.safefilms.data.Repository
import com.lis.safefilms.data.network.Service

object Injection {
    fun provideRepository(apiKey: String): Repository{
        return Repository(Service.create(apiKey))
    }
}