package com.example.surfgallery.common

sealed interface UiListState<out T> {
    object Loading : UiListState<Nothing>

    data class Success<T>(val data: List<T>) : UiListState<T>

    object Empty : UiListState<Nothing>

    data class Error(val uiError: UiError) : UiListState<Nothing>
}