package com.example.surfgallery.data.remote.picture

data class PictureResponse(
    val id: String,
    val title: String,
    val content: String,
    val photoUrl: String,
    val publicationDate: Int
)