package com.callme.feature.splash.domain.usecase

import com.callme.domain.storage.TokenStorage
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    operator fun invoke() = !tokenStorage.getToken().isNullOrBlank()
}