package com.example.surfgallery.data.remote.auth

data class UserInfo(
    val id: String,
    val phone: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val city: String,
    val about: String
)
