package com.callme.security.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.callme.di.scopes.ApplicationScope
import com.callme.domain.storage.TokenStorage
import com.callme.security.storage.SecureTokenStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
abstract class SecurityBindsModule {
    @Binds
    @ApplicationScope
    abstract fun bindTokenStorage(impl: SecureTokenStorage): TokenStorage
}

@Module
object SecurityProvidesModule {
    private const val ENCRYPTED_PREFS_NAME = "secure_prefs"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class EncryptedSharedPrefs

    @Provides
    @EncryptedSharedPrefs
    @ApplicationScope
    fun provideEncryptedSharedPreferences(context: Context): SharedPreferences {
        // TODO consider using DataStore + Tink since ESP is now deprecated (Phase II)
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
}