package com.example.surfgallery.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surfgallery.data.repository.DataBaseRepository
import com.example.surfgallery.data.repository.GalleryRepository
import com.example.surfgallery.domain.models.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val pictures = galleryRepository.pictures

    fun updateData() {
        galleryRepository.getPictures()
    }

    fun onFavoriteClick(picture: Picture) =
        viewModelScope.launch {
            if (picture.isInDatabase == true) {
                dataBaseRepository.deletePicture(picture.toEntity())
            } else {
                dataBaseRepository.insertPicture(picture.toEntity())
            }
        }
}