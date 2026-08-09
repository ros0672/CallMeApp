package com.callme.data.di

import com.callme.data.repository.AuthRepositoryImpl
import com.callme.di.scopes.ApplicationScope
import com.callme.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AuthDataModule {
    @Binds
    @ApplicationScope
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}