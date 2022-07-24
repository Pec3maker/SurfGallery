package com.example.surfgallery.data.repository

import com.example.surfgallery.data.local.PictureDao
import com.example.surfgallery.data.local.dto.PictureEntity
import com.example.surfgallery.data.local.dto.toPicture
import com.example.surfgallery.domain.models.Picture
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseRepository @Inject constructor(
    private val dao: PictureDao
) {
    fun getPictures(): Flow<List<PictureEntity>> = dao.getPictures()

    suspend fun getPictureById(id: String): Picture? = dao.getPictureById(id = id)?.toPicture()

    suspend fun insertPicture(picture: PictureEntity) {
        dao.insertPicture(picture = picture)
    }

    suspend fun insertAllPictures(pictures: List<PictureEntity>) {
        dao.insertAllPictures(pictures = pictures)
    }

    suspend fun deletePicture(picture: PictureEntity) {
        dao.deletePicture(picture = picture)
    }
}