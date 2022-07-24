package com.example.surfgallery.domain.models

data class Picture(
    val id: String,
    val title: String,
    val content: String,
    val photoUrl: String,
    val publicationDate: String,
    val isInDatabase: Boolean = false
)
