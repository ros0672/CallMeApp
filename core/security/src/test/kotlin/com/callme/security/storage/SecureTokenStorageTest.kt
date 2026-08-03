package com.callme.security.storage

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test

class SecureTokenStorageTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var secureTokenStorage: SecureTokenStorage

    @Before
    fun setUp() {
        sharedPreferences = mockk(relaxed = true)
        editor = mockk(relaxed = true)
        secureTokenStorage = SecureTokenStorage(sharedPreferences)
    }

    @Test
    fun `should save token to SharedPreferences`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor

        secureTokenStorage.saveToken("token")

        verify { editor.putString("access_token", "token") }
        verify { editor.apply() }
    }

    @Test
    fun `should return token from SharedPreferences`() {
        every { sharedPreferences.getString("access_token", null) } returns "test_token"
        val token = secureTokenStorage.getToken()

        assertEquals("test_token", token)
    }

    @Test
    fun `should clear token from SharedPreferences`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor

        secureTokenStorage.clearToken()

        verify { editor.clear() }
        verify { editor.apply() }
    }

    @Test
    fun `should return null if no token present in SharedPreferences`() {
        every { sharedPreferences.getString("access_token", null) } returns null
        val token = secureTokenStorage.getToken()

        assertNull(token)
    }
}