package com.callme.security.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.callme.domain.storage.TokenStorage
import com.callme.security.di.SecurityProvidesModule.EncryptedSharedPrefs
import javax.inject.Inject

class SecureTokenStorage @Inject constructor(
    @EncryptedSharedPrefs private val preferences: SharedPreferences
) : TokenStorage {
    override fun getToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun saveToken(token: String) {
        preferences.edit { putString(KEY_ACCESS_TOKEN, token) }
    }

    override fun clearToken() {
        preferences.edit { clear() }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
}