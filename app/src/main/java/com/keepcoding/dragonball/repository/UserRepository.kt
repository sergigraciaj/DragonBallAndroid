package com.keepcoding.dragonball.repository

import org.jetbrains.annotations.VisibleForTesting
import kotlin.random.Random

class UserRepository {

    companion object {
        private var token = ""
    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        data class Error(val message: String) : LoginResponse()
    }

    fun login(user: String, password: String): LoginResponse {
        return if (Random.nextBoolean()) {
            token = "eyJraWQiOiJwcml2YXRlIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZGVudGlmeSI6IjdBQjhBQzRELUFEOEYtNEFDRS1BQTQ1LTIxRTg0QUU4QkJFNyIsImVtYWlsIjoiYmVqbEBrZWVwY29kaW5nLmVzIiwiZXhwaXJhdGlvbiI6NjQwOTIyMTEyMDB9.Dxxy91hTVz3RTF7w1YVTJ7O9g71odRcqgD00gspm30s"
            LoginResponse.Success
        } else {
            LoginResponse.Error("501")
        }
    }

    fun getToken(): String = token

    @VisibleForTesting
    fun setToken(token: String) { UserRepository.token = token }
}