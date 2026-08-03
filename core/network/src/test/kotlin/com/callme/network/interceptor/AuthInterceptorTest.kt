package com.callme.network.interceptor

import com.callme.domain.storage.TokenStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var tokenStorage: TokenStorage
    private lateinit var chain: Interceptor.Chain

    @Before
    fun setUp() {
        tokenStorage = mockk(relaxed = true)
        chain = mockk(relaxed = true)
        authInterceptor = AuthInterceptor(tokenStorage)
    }

    @Test
    fun `should add Authorization header when token is present`() {
        val request = Request.Builder().url("https://test.com").build()

        every { chain.request() } returns request
        every { tokenStorage.getToken() } returns "test_token"

        val slot = slot<Request>()

        authInterceptor.intercept(chain)

        verify { chain.proceed(capture(slot)) }
        val capturedRequest = slot.captured
        assertEquals("Bearer test_token", capturedRequest.header("Authorization"))
    }

    @Test
    fun `should not add Authorization header when token is null`() {
        val request = Request.Builder().url("https://test.com").build()

        every { chain.request() } returns request
        every { tokenStorage.getToken() } returns null

        authInterceptor.intercept(chain)

        verify { chain.proceed(request) }
    }
}