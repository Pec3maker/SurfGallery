package com.example.surfgallery.restapi.interceptor

import com.example.surfgallery.data.repository.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .header(
                name = AUTHORIZATION,
                value = "Token ${dataStoreRepository.userPreferences.value?.token}"
            )
            .build()
        return chain.proceed(request = request)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}