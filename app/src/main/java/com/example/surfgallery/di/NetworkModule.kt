package com.example.surfgallery.di

import com.example.surfgallery.BuildConfig
import com.example.surfgallery.restapi.AuthenticationService
import com.example.surfgallery.restapi.GalleryService
import com.example.surfgallery.restapi.authenticator.TokenAuthenticator
import com.example.surfgallery.restapi.interceptor.TokenInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()


    @Singleton
    @Provides
    fun provideAuthenticationService(
        builder: OkHttpClient.Builder,
        moshi: Moshi,
        tokenInterceptor: TokenInterceptor
    ): AuthenticationService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(
            builder.apply {
                addInterceptor(tokenInterceptor)
            }.build()
        )
        .build().create(AuthenticationService::class.java)

    @Singleton
    @Provides
    fun provideGalleryService(
        builder: OkHttpClient.Builder,
        moshi: Moshi,
        tokenInterceptor: TokenInterceptor,
        authenticator: TokenAuthenticator
    ): GalleryService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(
            builder.apply {
                authenticator(authenticator)
                addInterceptor(tokenInterceptor)
            }.build()
        )
        .build().create(GalleryService::class.java)
}
