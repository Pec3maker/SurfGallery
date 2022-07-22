package com.example.surfgallery.restapi

import com.example.surfgallery.data.remote.picture.PictureResponse
import retrofit2.http.GET

interface GalleryService {

    @GET("/picture")
    suspend fun getPicture(): List<PictureResponse>
}