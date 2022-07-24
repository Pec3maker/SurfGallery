package com.example.surfgallery.domain.models

import com.example.surfgallery.data.local.dto.PictureEntity

data class Picture(
    val id: String,
    val title: String,
    val content: String,
    val photoUrl: String,
    val publicationDate: String,
    val isInDatabase: Boolean? = null
) {
    fun toEntity() = PictureEntity(
        id = id,
        title = title,
        content = content,
        photoUrl = photoUrl,
        publicationDate = publicationDate
    )
}