package com.example.surfgallery.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surfgallery.common.UiListState
import com.example.surfgallery.data.repository.DataBaseRepository
import com.example.surfgallery.data.repository.GalleryRepository
import com.example.surfgallery.domain.models.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    galleryRepository: GalleryRepository
) : ViewModel() {

    private val state = galleryRepository.pictures.value
    private val pictures = if (state is UiListState.Success) state.data else emptyList()


    fun onFavoriteClick(picture: Picture) =
        viewModelScope.launch {
            if (picture.isInDatabase == true) {
                dataBaseRepository.deletePicture(picture.toEntity())
            } else {
                dataBaseRepository.insertPicture(picture.toEntity())
            }
        }

    fun getFilteredPictures(query: String) = pictures.filter { picture ->
        picture.title.contains(other = query, ignoreCase = true)
    }
}