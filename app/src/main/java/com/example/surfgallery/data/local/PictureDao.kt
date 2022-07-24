package com.example.surfgallery.data.local

import androidx.room.*
import com.example.surfgallery.data.local.dto.PictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Query("SELECT * FROM PictureEntity")
    fun getPictures(): Flow<List<PictureEntity>>

    @Query("SELECT * FROM PictureEntity WHERE id = :id")
    suspend fun getPictureById(id: String): PictureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPictures(pictures: List<PictureEntity>)

    @Delete
    suspend fun deletePicture(picture: PictureEntity)
}