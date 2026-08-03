package com.callme.network.interceptor

import com.callme.domain.storage.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = tokenStorage.getToken()?.let {
            request.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: request

        return chain.proceed(newRequest)
    }
}