package com.example.surfgallery.data.repository

import com.example.surfgallery.common.UiError
import com.example.surfgallery.common.UiListState
import com.example.surfgallery.data.local.dto.PictureEntity
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
    private val scope: CoroutineScope,
    private val dataBaseRepository: DataBaseRepository
) {
    private var job: Job? = null

    private val _picturesFlow = MutableStateFlow(emptyList<PictureEntity>())
    val picturesFlow: StateFlow<List<PictureEntity>> = _picturesFlow

    private val _pictures = MutableStateFlow<UiListState<Picture>>(UiListState.Loading)
    val pictures: StateFlow<UiListState<Picture>> = _pictures

    init {
        scope.launch {
            dataBaseRepository.getPictures().collect {
                _picturesFlow.emit(it)
            }
        }
        getPictures()
        onDatabaseUpdate()
    }

    private fun onDatabaseUpdate() {
        scope.launch {
            _picturesFlow.collect {
                val pictureState = _pictures.value
                if (pictureState is UiListState.Success) {
                    _pictures.value = fillPicturesAccessory(pictureState.data)
                }
            }
        }
    }

    fun getPictures() {
        job?.cancel()
        job = scope.launch {
            _pictures.emit(UiListState.Loading)
            try {
                val data = galleryService.getPictures().map { it.toPictureModel() }
                _pictures.emit(fillPicturesAccessory(data))
            } catch (e: CancellationException) {

            } catch (e: Exception) {
                _pictures.emit(UiListState.Error(uiError = UiError.onGallery(exception = e)))
            }
        }
    }

    private fun fillPicturesAccessory(filmList: List<Picture>): UiListState.Success<Picture> {
        val changedFilmList = filmList.toMutableList().apply {
            replaceAll { film ->
                film.copy(isInDatabase = _picturesFlow.value.any { it.id == film.id })
            }
        }
        return UiListState.Success(changedFilmList)
    }
}