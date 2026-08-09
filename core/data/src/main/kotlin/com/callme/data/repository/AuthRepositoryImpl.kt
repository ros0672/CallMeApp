package com.callme.data.repository

import com.callme.domain.repository.AuthRepository
import com.callme.domain.storage.TokenStorage
import com.callme.domain.utils.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        // TODO CallMeApp #15: replace with real authApi login call
        delay(1000L)
        tokenStorage.saveToken("fake_token_for_$email")
        return Result.Success(Unit)
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        // TODO CallMeApp #15: replace with real authApi register call
        delay(1000L)
        tokenStorage.saveToken("fake_token_for_$email")
        // Test error
        return Result.Error("Please check your email and password")

    }

    override suspend fun logout() {
        tokenStorage.clearToken()
    }
}