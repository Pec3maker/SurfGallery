package com.example.surfgallery.data.remote.auth

import com.squareup.moshi.Json

data class AuthResponse(
    val token: String,
    @Json(name = "user_info") val userInfo: UserInfo
)
