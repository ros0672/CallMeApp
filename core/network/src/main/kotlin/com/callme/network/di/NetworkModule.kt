package com.callme.network.di

import com.callme.di.scopes.ApplicationScope
import com.callme.network.BuildConfig
import com.callme.network.api.AuthApi
import com.callme.network.interceptor.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @Provides
    @ApplicationScope
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Logs are visible for DEBUG builds only
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    // TODO: use 2 clients (authOkHttpClient & apiOkHttpClient) with corresponding @NetworkQualifiers (Phase II)
    @Provides
    @ApplicationScope
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://callmeapp.com/api") // TODO CallMeApp #15: replace with actual URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    // AuthApi
    @Provides
    @ApplicationScope
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    // Moshi
    // KotlinJsonAdapterFactory is required for data classes serialization
    @Provides
    @ApplicationScope
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}