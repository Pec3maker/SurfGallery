package com.example.surfgallery.common

import androidx.annotation.StringRes
import com.example.surfgallery.R
import retrofit2.HttpException

sealed class UiError(
    override val message: String? = null,
    @StringRes val messageRes: Int = R.string.unknown_error_message,
    @StringRes val snackbarMessageRes: Int? = null
) : Throwable(message) {

    object UnknownHostException : UiError(
        messageRes = R.string.unknown_host_error_message,
        snackbarMessageRes = R.string.unknown_host_error_message,
    )

    object UnknownAuthenticationException :
        UiError(
            snackbarMessageRes = R.string.unknown_error_message
        )

    object InvalidCredentials : UiError(
        snackbarMessageRes = R.string.invalid_credentials_error_message
    )

    object UserIsDisabled : UiError(
        snackbarMessageRes = R.string.user_is_disabled_error_message
    )

    object WrongLoginLength : UiError(messageRes = R.string.wrong_login_length_error_message)

    object EmptyLogin : UiError(messageRes = R.string.empty_login_error_message)

    object WrongPasswordLength : UiError(messageRes = R.string.wrong_password_length_error_message)

    object EmptyPassword : UiError(messageRes = R.string.empty_password_error_message)

    data class Common(override val message: String? = null) : UiError(
        snackbarMessageRes = R.string.unknown_error_message
    )

    companion object {
        fun common(exception: Throwable): UiError =
            when (exception) {
                is java.net.UnknownHostException -> UnknownHostException
                else -> Common(exception.message)
            }

        fun onAuthentication(exception: Throwable): UiError =
            if (exception is HttpException) {
                when (exception.code()) {
                    400 -> InvalidCredentials
                    401 -> UserIsDisabled
                    else -> UnknownAuthenticationException
                }
            } else {
                common(exception)
            }

        fun onGallery(exception: Throwable): UiError =
            if (exception is HttpException) {
                when (exception.code()) {
                    401 -> UserIsDisabled
                    else -> UnknownHostException
                }
            } else {
                common(exception)
            }
    }
}