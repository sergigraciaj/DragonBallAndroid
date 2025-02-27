package com.keepcoding.dragonball.repository

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.EMPTY_REQUEST
import org.jetbrains.annotations.VisibleForTesting

class UserRepository {
    private val BASE_URL = "https://dragonball.keepcoding.education/api/"

    companion object {
        private var token = ""
    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        data class Error(val message: String) : LoginResponse()
    }

    private val client = OkHttpClient()
    private val url = "${BASE_URL}auth/login"

    fun login(user: String, password: String): LoginResponse {
        val credentials = Credentials.basic(user, password)

        val empty: RequestBody = EMPTY_REQUEST
        val request = Request.Builder()
            .url(url)
            .post(EMPTY_REQUEST)
            .addHeader("Authorization", credentials)
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        return if (response.isSuccessful) {
            token = response.body!!.string()
            LoginResponse.Success
        } else {
            LoginResponse.Error("501")
        }
    }

    fun getToken(): String = token

    @VisibleForTesting
    fun setToken(token: String) { UserRepository.token = token }
}