package com.callme.feature.auth.domain.usecase

import com.callme.domain.repository.AuthRepository
import com.callme.domain.utils.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password)
    }
}