package com.callme.domain.repository

import com.callme.domain.utils.Result
interface AuthRepository {
    // TODO CallMeApp #15: DTOs will be introduced during further backend integration (Phase II)
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun logout()
}