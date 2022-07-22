package com.example.surfgallery.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surfgallery.R
import com.example.surfgallery.common.UiError
import com.example.surfgallery.common.UiState
import com.example.surfgallery.data.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    private var isErrorLogin = false
    private var isErrorPassword = false
    private var login: String = ""
    private var password: String = ""

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    var uiState: StateFlow<UiState<Unit>> = _uiState

    private val _showSnackbar = MutableSharedFlow<Int>()
    val showSnackbar: SharedFlow<Int> = _showSnackbar

    private var _loginErrorMessage = MutableSharedFlow<Int>()
    var loginErrorMessage: SharedFlow<Int> = _loginErrorMessage

    private var _passwordErrorMessage = MutableSharedFlow<Int>()
    var passwordErrorMessage: SharedFlow<Int> = _passwordErrorMessage

    fun updateLogin(login: String) {
        this.login = login
        clearLoginError()
    }

    fun updatePassword(password: String) {
        this.password = password
        clearPasswordError()
    }

    private fun clearLoginError() {
        isErrorLogin = false
        viewModelScope.launch {
            _loginErrorMessage.emit(R.string.empty_string)
        }
    }

    private fun clearPasswordError() {
        isErrorPassword = false
        viewModelScope.launch {
            _passwordErrorMessage.emit(R.string.empty_string)
        }
    }

    private suspend fun setLoginError(message: Int) {
        isErrorLogin = true
        _loginErrorMessage.emit(message)
    }

    private suspend fun setPasswordError(message: Int) {
        isErrorPassword = true
        _passwordErrorMessage.emit(message)
    }

    fun onEnterClick() {
        clearLoginError()
        clearPasswordError()

        viewModelScope.launch {
            if (login.isEmpty()) {
                setLoginError(UiError.EmptyLogin.messageRes)
            } else if (login.length < LOGIN_MIN_LENGTH) {
                setLoginError(UiError.WrongLoginLength.messageRes)
            }

            if (password.isEmpty()) {
                setPasswordError(UiError.EmptyPassword.messageRes)
            } else if (password.length !in PASSWORD_LENGTH_BOUNDARIES) {
                setPasswordError(UiError.WrongPasswordLength.messageRes)
            }

            if (!isErrorLogin && !isErrorPassword) {
                _uiState.emit(UiState.Loading)

                try {
                    repository.login(login, password)
//                    _onLogin.emit(
//                        todo navigate gallery
//                    )
                } catch (e: Exception) {
                    val error = UiError.onAuthentication(e)
                    if (error.snackbarMessageRes != null) {
                        _showSnackbar.emit(error.snackbarMessageRes)
                    } else {
                        setLoginError(R.string.empty_string)
                        setPasswordError(error.messageRes)
                    }
                }
                _uiState.emit(UiState.Success(Unit))
            }
        }
    }

    companion object {
        private const val LOGIN_MIN_LENGTH = 12
        private val PASSWORD_LENGTH_BOUNDARIES = 6..256
    }
}