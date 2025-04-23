package com.example.networksignalapp.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val user_name: String, val password: String)
data class RegisterRequest(val user_name: String, val password: String)
data class TokenResponse(val token: String)

interface AuthService {
    @POST("user")
    suspend fun register(@Body request: RegisterRequest): Response<Map<String, Any>>

    @POST("authentication")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>
}
