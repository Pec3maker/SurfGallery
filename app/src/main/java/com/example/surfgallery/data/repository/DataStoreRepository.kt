package com.example.surfgallery.data.repository

import androidx.datastore.core.DataStore
import com.example.surfgallery.UserInformation
import com.example.surfgallery.data.remote.auth.AuthResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<UserInformation>,
    coroutineScope: CoroutineScope
) {
    private val _userPreferences = MutableStateFlow<UserInformation?>(null)
    val userPreferences: StateFlow<UserInformation?> = _userPreferences

    private val authFlow = dataStore.data

    init {
        coroutineScope.launch {
            authFlow.collect(_userPreferences::emit)
        }
    }

    suspend fun saveUserPreferences(authInformation: AuthResponse?) =
        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setToken(authInformation?.token ?: "")
                .setId(authInformation?.userInfo?.id ?: "")
                .setPhone(authInformation?.userInfo?.phone ?: "")
                .setEmail(authInformation?.userInfo?.email ?: "")
                .setFirstName(authInformation?.userInfo?.firstName ?: "")
                .setLastName(authInformation?.userInfo?.lastName ?: "")
                .setAvatar(authInformation?.userInfo?.avatar ?: "")
                .setCity(authInformation?.userInfo?.city ?: "")
                .setAbout(authInformation?.userInfo?.about ?: "")
                .build()
        }

    suspend fun clear() {

        saveUserPreferences(authInformation = null)
    }
}