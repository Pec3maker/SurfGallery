package com.example.surfgallery.ui.pictureinfo

import androidx.lifecycle.ViewModel
import com.example.surfgallery.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PictureInfoViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    fun getPictureInfo(id: String) = galleryRepository.loadedPictures.find { id == it.id }
}