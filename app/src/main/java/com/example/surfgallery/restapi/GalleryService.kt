package com.example.surfgallery.restapi

import com.example.surfgallery.data.remote.picture.PictureResponse
import retrofit2.http.GET

interface GalleryService {

    @GET("/api/picture")
    suspend fun getPictures(): List<PictureResponse>
}