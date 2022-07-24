package com.example.surfgallery.data.repository

import com.example.surfgallery.common.UiError
import com.example.surfgallery.common.UiListState
import com.example.surfgallery.domain.models.Picture
import com.example.surfgallery.restapi.GalleryService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryRepository @Inject constructor(
    private val galleryService: GalleryService,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    private val _pictures = MutableStateFlow<UiListState<Picture>>(UiListState.Loading)
    val pictures: StateFlow<UiListState<Picture>> = _pictures

    init {
        getPictures()
    }

    fun getPictures() {
        job?.cancel()
        job = scope.launch {
            _pictures.emit(UiListState.Loading)
            try {
                _pictures.emit(
                    UiListState.Success(galleryService.getPictures().map { it.toPictureModel() })
                    //todo match data for gallery with db
                )
            } catch (e: CancellationException) {

            } catch (e: Exception) {
                _pictures.emit(UiListState.Error(uiError = UiError.onGallery(exception = e)))
            }
        }
    }
}