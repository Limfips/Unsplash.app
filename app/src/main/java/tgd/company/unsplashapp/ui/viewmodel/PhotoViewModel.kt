package tgd.company.unsplashapp.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tgd.company.unsplashapp.data.photo.Photo
import tgd.company.unsplashapp.data.search.SearchResult
import tgd.company.unsplashapp.other.Event
import tgd.company.unsplashapp.other.Resource
import tgd.company.unsplashapp.service.repository.IPhotoRepository
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: IPhotoRepository
): ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var fab: FloatingActionButton? = null

    var closeBehavior: (() -> Boolean)? = null

    private val _selectedPhoto = MutableLiveData<Photo?>()
    val selectedPhoto: LiveData<Photo?> = _selectedPhoto

    fun setSelectedPhoto(photo: Photo?) {
        _selectedPhoto.postValue(photo)
    }

    private val _randomPhoto = MutableLiveData<Event<Resource<Photo>>>()
    val randomPhoto: LiveData<Event<Resource<Photo>>> = _randomPhoto

    fun getRandomPhoto() {
        _randomPhoto.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = photoRepository.getRandom()
            _randomPhoto.value = Event(response)
        }
    }

    private val _searchPhotos = MutableLiveData<Event<Resource<SearchResult>>>()
    val searchPhotos: LiveData<Event<Resource<SearchResult>>> = _searchPhotos

    fun searchPhoto(query: String, page: Int) {
        if (query.isEmpty()) {
            return
        }
        _searchPhotos.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = photoRepository.searchPhoto(query, page)
            _searchPhotos.value = Event(response)
        }
    }


}