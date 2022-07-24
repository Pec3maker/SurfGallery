package com.example.surfgallery.ui.gallery

import androidx.lifecycle.ViewModel
import com.example.surfgallery.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    val pictures = galleryRepository.pictures

    fun updateData() {
        galleryRepository.getPictures()
    }
}