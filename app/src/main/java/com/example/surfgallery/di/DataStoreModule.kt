package com.example.surfgallery.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.surfgallery.UserInformation
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

private val userSerializer = object : Serializer<UserInformation> {
    override val defaultValue: UserInformation = UserInformation.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserInformation {
        try {
            return UserInformation.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserInformation, output: OutputStream) = t.writeTo(output)
}

private val Context.dataStore: DataStore<UserInformation> by dataStore(
    fileName = "user_info.pb",
    serializer = userSerializer
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<UserInformation> =
        context.dataStore
}