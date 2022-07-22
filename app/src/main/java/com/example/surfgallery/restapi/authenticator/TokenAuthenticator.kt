package com.example.surfgallery.restapi.authenticator

import com.example.surfgallery.data.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        authenticationRepository.clear()
        null
    }
}
