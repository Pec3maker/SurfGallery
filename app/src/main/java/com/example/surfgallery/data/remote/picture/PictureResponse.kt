package com.example.surfgallery.data.remote.picture

import com.example.surfgallery.domain.models.Picture

data class PictureResponse(
    val id: String,
    val title: String,
    val content: String,
    val photoUrl: String,
    val publicationDate: String
) {
    fun toPictureModel() = Picture(
        id = id,
        title = title,
        content = content,
        photoUrl = photoUrl,
        publicationDate = publicationDate
    )
}