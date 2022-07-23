package com.example.surfgallery.ui.main

import androidx.lifecycle.ViewModel
import com.example.surfgallery.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        runBlocking {
            delay(timeMillis = 800)
            _isLoading.value = false
        }
    }

    fun isTokenContained() = !dataStoreRepository.userPreferences.value?.token.isNullOrEmpty()
}