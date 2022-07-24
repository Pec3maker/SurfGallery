package com.example.surfgallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.surfgallery.data.local.dto.PictureEntity

@Database(
    entities = [PictureEntity::class],
    version = 1
)
abstract class GalleryDatabase : RoomDatabase() {

    abstract val pictureDao: PictureDao
}