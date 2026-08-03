package com.callme.network.model

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String? = null
)