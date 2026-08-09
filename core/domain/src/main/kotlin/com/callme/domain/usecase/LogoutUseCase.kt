package com.callme.domain.usecase

import com.callme.domain.repository.AuthRepository

// TODO CallMeApp #16: move to :feature:profile
class LogoutUseCase /*@Inject*/ constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return authRepository.logout()
    }
}