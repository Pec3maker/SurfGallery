package com.example.surfgallery.data.repository

import com.example.surfgallery.data.remote.auth.AuthBody
import com.example.surfgallery.restapi.AuthenticationService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authenticationService: AuthenticationService
) {
    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent

    suspend fun login(phone: String, password: String) {
        val authInformation = authenticationService.login(
            AuthBody(
                phone = phone,
                password = password
            )
        )
        dataStoreRepository.saveUserPreferences(authInformation = authInformation)
    }

    suspend fun logout() {
        try {
            authenticationService.logout()
        } catch (e: Exception) {

        }
        clear()
    }

    suspend fun clear() {
        dataStoreRepository.clear()
        onLogoutEvent()
    }

    private suspend fun onLogoutEvent() {
        _logoutEvent.emit(Unit)
    }
}