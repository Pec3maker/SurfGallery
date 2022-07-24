package com.example.surfgallery.ui.pictureinfo

import androidx.lifecycle.ViewModel
import com.example.surfgallery.common.UiListState
import com.example.surfgallery.data.repository.GalleryRepository
import com.example.surfgallery.domain.models.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PictureInfoViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    private val state = galleryRepository.pictures.value

    fun getPictureInfo(id: String): Picture? {
        return if (state is UiListState.Success) {
            state.data.find { id == it.id }
        } else {
            null
        }
    }
}