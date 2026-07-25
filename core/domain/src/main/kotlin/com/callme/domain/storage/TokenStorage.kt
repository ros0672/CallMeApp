package com.callme.domain.storage

interface TokenStorage {
    // TODO add refresh token logic (Phase II)
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}