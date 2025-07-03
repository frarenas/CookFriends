package com.devapp.cookfriends.di

import com.devapp.cookfriends.data.remote.service.CookFriendsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideKotlinXSerialization(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideExampleApiService(
        client: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): CookFriendsService = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(jsonConverterFactory)
        .client(client)
        .build()
        .create(CookFriendsService::class.java)

    private companion object {
        val API_BASE_URL = "https://script.google.com/macros/s/AKfycbzYa1RDPseB3M2gtqRqZc7qWr7hSg1G43EEjYytrdDbPICWvneQ5xA0BPJncnyCHU1s/"
    }
}
