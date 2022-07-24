package com.example.surfgallery.restapi

import com.example.surfgallery.data.remote.auth.AuthBody
import com.example.surfgallery.data.remote.auth.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST("/api/auth/login")
    suspend fun login(@Body authBody: AuthBody): AuthResponse

    @POST("/api/auth/logout")
    suspend fun logout(): Response<Unit>
}