package com.example.surfgallery.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.surfgallery.domain.models.Picture

@Entity
data class PictureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val photoUrl: String,
    val publicationDate: String
)

fun PictureEntity.toPicture() =
    Picture(
        id = id,
        title = title,
        content = content,
        photoUrl = photoUrl,
        publicationDate = publicationDate,
        isInDatabase = true
    )