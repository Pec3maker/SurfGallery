package com.example.surfgallery.di

import android.content.Context
import androidx.room.Room
import com.example.surfgallery.data.local.GalleryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    private const val DATABASE_NAME = "galleryDatabase"

    @Provides
    @Singleton
    fun provideGalleryDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, GalleryDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFilmDao(db: GalleryDatabase) = db.pictureDao
}