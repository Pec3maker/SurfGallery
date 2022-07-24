package com.example.surfgallery.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surfgallery.data.repository.AuthenticationRepository
import com.example.surfgallery.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val user = dataStoreRepository.getUser()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}