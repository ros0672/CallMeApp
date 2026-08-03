package com.callme.network.api

import com.callme.network.model.LoginRequest
import com.callme.network.model.LoginResponse
import com.callme.network.model.RefreshTokenRequest
import com.callme.network.model.RefreshTokenResponse
import com.callme.network.model.RegisterRequest
import com.callme.network.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): RefreshTokenResponse
}