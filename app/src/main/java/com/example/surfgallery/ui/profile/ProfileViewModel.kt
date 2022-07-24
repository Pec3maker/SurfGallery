package com.example.surfgallery.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surfgallery.R
import com.example.surfgallery.common.UiState
import com.example.surfgallery.data.repository.AuthenticationRepository
import com.example.surfgallery.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val user = dataStoreRepository.getUser()

    private val _showSnackbar = MutableSharedFlow<Int>()
    val showSnackbar: SharedFlow<Int> = _showSnackbar

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState

    fun logout() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                authenticationRepository.logout()
            } catch (e: Exception) {
                _showSnackbar.emit(R.string.logout_error_label)
            }
            _uiState.value = UiState.Success(Unit)
        }
    }
}