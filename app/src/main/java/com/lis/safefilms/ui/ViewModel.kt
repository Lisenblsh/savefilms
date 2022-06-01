package com.lis.safefilms.ui

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.lis.safefilms.data.KinopoiskAPIModel
import com.lis.safefilms.data.Repository
import com.lis.safefilms.tools.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class FilmViewModel(
    private val repository: Repository,
) : ViewModel() {
    val filmData: MutableLiveData<Resource<KinopoiskAPIModel>> = MutableLiveData()

    fun getFilmData(kinopoiskId: Int) = viewModelScope.launch {
        filmData.postValue(Resource.Loading())
        try{
            val response = repository.getFilm(kinopoiskId)
            filmData.postValue(handlerFilmData(response))
        } catch (e: HttpException){
            filmData.postValue(Resource.Error(e.message()))
        } catch (e: Exception) {
            filmData.postValue(Resource.Error("${e.message}"))
        }

    }

    private fun handlerFilmData(response: Response<KinopoiskAPIModel>): Resource<KinopoiskAPIModel> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: Repository
    ) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if(modelClass.isAssignableFrom(FilmViewModel::class.java)){
            return FilmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}